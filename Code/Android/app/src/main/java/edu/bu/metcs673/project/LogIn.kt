package edu.bu.metcs673.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
// import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {


    // Global Variable Namespace
    // ********************************

    lateinit var gso: GoogleSignInOptions

    lateinit var signIn: SignInButton

    lateinit var googleSignInClient:GoogleSignInClient

    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    var RC_SIGN_IN=1

    lateinit var auth: FirebaseAuth

    lateinit var mAuthListener:FirebaseAuth.AuthStateListener


/*
    override fun onStart() {
        super.onStart()

        val currentUser:FirebaseUser?=auth.currentUser
        updateUI(currentUser)
    }
*/

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // Configure Google Sign In
        gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        signIn=findViewById<View>(R.id.googleBtn) as SignInButton

        googleSignInClient= GoogleSignIn.getClient(this,gso)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()


        signIn.setOnClickListener{

            view: View?->signIn()
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
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
                //firebaseAuthWithGoogle(account)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                println("No error from onActivityResult function call.")
                Log.w("TAG", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun updateUI(user:FirebaseUser?){
        if (user==null){
            Log.w("TAG", "User not signed in.")
            return
        }
        val intent=Intent(this, MainActivity::class.java)
        startActivity(intent)
        //finish()
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)

                    updateUI(null)
                }

            }

    }

}