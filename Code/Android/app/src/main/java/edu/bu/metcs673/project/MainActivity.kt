package edu.bu.metcs673.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bu.metcs673.project.model.user.User
import edu.bu.metcs673.project.ui.base.BaseActivity


// @class MainActivity
// @brief Class to handle main configurations for chat application.
//      Instantiated upon application login.

class MainActivity : BaseActivity() {

    // Global variable namespace
    companion object {
        private const val TAG = "MainActivity"
        lateinit var currentUser: User
    }

    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var auth: FirebaseAuth

    // @function OnCreate
    // @brief Default function called when class is instantiated.
    //      Set up following instances:
    //      1) navigation controller
    //      2) google signin
    //      3) firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_chat, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        auth = FirebaseAuth.getInstance()
        val userId = auth.uid
        userId?.let { currentUserId ->
            Firebase.firestore.collection("users").document(currentUserId).get().addOnSuccessListener {
                currentUser = User(it)
            }
        }
    }

    // @function OnCreateOptionsMenu
    // @brief Create sign out option.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sign_out, menu)
        return true
    }

    // @function onOptionsItemSelected
    // @brief Called when user clicks sign out button.
    //      Create Intent to send user back to log in page.
    //      Sign out users from google and firebase accounts.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miSignOut) {
            Log.i(TAG, "Logout")

            // Log out the user
            auth.signOut()
            // sign out from google too
            googleSignInClient.signOut()

            //Navigate Back to Sign In Screen
            val logoutIntent = Intent(this, LogInActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
        }
        return super.onOptionsItemSelected(item)
    }

}