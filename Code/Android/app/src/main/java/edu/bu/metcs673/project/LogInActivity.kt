package edu.bu.metcs673.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import edu.bu.metcs673.project.core.ICApp
import edu.bu.metcs673.project.model.user.User
import edu.bu.metcs673.project.ui.base.BaseActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.logging.Logger
import javax.inject.Inject


// @class MainActivity
// @brief Class to handle log in page for UI.
//      Instantiated upon application login.
//      Implement Google Login

class LogInActivity : BaseActivity() {

    // Global Variable namespace
    companion object {
        private const val TAG = "LogInActivity"
    }

    lateinit var signIn: SignInButton

    private val RC_SIGN_IN = 1

    lateinit var auth: FirebaseAuth

    // @function OnCreate
    // @brief Default function called when class is instantiated.
    //      Google sign in, and firebase instance created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        signIn = findViewById<View>(R.id.googleBtn) as SignInButton

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        signIn.setOnClickListener { signIn() }

        if (auth.currentUser != null) {
            updateUI(auth.currentUser, false)
            return
        }
    }

    // @function signIn
    // @brief Create intent that signs in user
    private fun signIn() {
        val signInIntent = (application as ICApp).getGoogleSignInClient().signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    // @function onActivityResult
    // @brief Assess if log in was successful. If not, log the error.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            //val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                task.addOnSuccessListener {account ->
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                }.addOnFailureListener {
                    Log.e(TAG, "failed to sign in", it)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed", e)
            }
        }
    }

    // @function updateUI
    // @brief When user log in, navigate from log in screen to mainactivity screen.
    //      Call updateUserDatatoFirebase to store user account info.
    private fun updateUI(user: FirebaseUser?, isFirstTime: Boolean) {
        if (user == null) {
            Log.e("TAG", "User not signed in.")
            return
        }
        updateUserDataToFirestore(user, isFirstTime)
    }

    private fun processToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    // @function updateUserDataToFirestore
    // @brief Store user data as a child node in Firebase cloud database.
    private fun updateUserDataToFirestore(user: FirebaseUser?, isFirstTime: Boolean = false) {
        if (user == null || user.displayName == null) {
            return
        }
        val userToAdd = mutableMapOf<String, Any?>(
            "name" to user.displayName,
            "email" to user.email,
            "updatedAt" to Timestamp.now(),
            "uid" to user.uid
        )
        if (isFirstTime) {
            userToAdd["createdAt"] = Timestamp.now()
            userToAdd["profile_picture"] = if (user.photoUrl != null) user.photoUrl.toString() else ""
        }
        userApi.loginUser(User(user.uid, userToAdd)).enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                // show error
                Log.e(TAG, t.message, t)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                processToMainActivity()
            }

        })
    }


    // @function firebaseAuthWithGoogle
    // @brief Assess if firebase authentication was successful. If so, update UI
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user, true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    updateUI(null, false)
                }
            }
    }
}