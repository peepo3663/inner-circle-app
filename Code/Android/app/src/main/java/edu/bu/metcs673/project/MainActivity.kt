package edu.bu.metcs673.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
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
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {

    // Variable Namespace
    // ********************************

    val navView: BottomNavigationView = findViewById(R.id.nav_view)

    val navController = findNavController(R.id.nav_host_fragment)

    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    val appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
        )
    )

    lateinit var mGoogleSignInClient: GoogleSignInClient

    lateinit var gso: GoogleSignInOptions

    val RC_SIGN_IN: Int = 1

    lateinit var signOut: Button

    // ********************************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* val signIn = findViewById<View>(R.id.signInBtn) as SignInButton
         gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
             .requestIdToken(getString(R.string.default_web_client_id))
             .requestEmail()
             .build()


         signOut = findViewById<View>(R.id.signOutBtn) as Button
         signOut.visibility = View.INVISIBLE
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

         signIn.setOnClickListener {
                 view: View? -> signIn()
         }
          */

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        } else {
            Toast.makeText(this,"Problem in execution order :(", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleResult (completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            updateUI (account!!)
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {

        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener {
                view: View? -> mGoogleSignInClient.signOut().addOnCompleteListener {
                task: Task<Void> -> if (task.isSuccessful)
                {
                    signOut.visibility = View.INVISIBLE
                    signOut.isClickable = false
                }
            }

        }
    }

}