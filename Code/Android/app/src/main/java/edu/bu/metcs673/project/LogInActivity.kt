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

// import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LogInActivity"
    }

    lateinit var signIn: SignInButton

    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    var RC_SIGN_IN = 1

    lateinit var auth: FirebaseAuth

    lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    val userDocumentRef = Firebase.firestore.collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        signIn = findViewById<View>(R.id.googleBtn) as SignInButton

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        signIn.setOnClickListener { signIn() }

        if (auth.currentUser != null) {
            updateUI(auth.currentUser, false)
            return
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


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

    private fun updateUI(user: FirebaseUser?, isFirstTime: Boolean) {
        if (user == null) {
            Log.e("TAG", "User not signed in.")
            return
        }
        updateUserDataToFirestore(user, isFirstTime)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        //finish()
    }

    private fun updateUserDataToFirestore(user: FirebaseUser?, isFirstTime: Boolean = false) {
        if (user == null || user.displayName == null) {
            return
        }
        val userToAdd = hashMapOf(
            "name" to user.displayName,
            "email" to user.email,
            "updatedAt" to Timestamp.now()
        )
        if (isFirstTime) {
            userToAdd["createdAt"] = Timestamp.now()
            userToAdd["profile_picture"] = if (user.photoUrl != null) user.photoUrl.toString() else ""
        }
        userDocumentRef.document(user.uid).set(userToAdd, SetOptions.merge()).addOnSuccessListener {
        }.addOnFailureListener {
            Log.e(TAG, "can not add this user", it)
        }
    }


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