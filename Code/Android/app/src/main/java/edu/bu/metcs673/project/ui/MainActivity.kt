package edu.bu.metcs673.project.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.core.ICApp
import edu.bu.metcs673.project.model.message.ChatRoomModel
import edu.bu.metcs673.project.model.user.User
import edu.bu.metcs673.project.ui.base.BaseActivity
import edu.bu.metcs673.project.ui.chat.ChatActivity
import edu.bu.metcs673.project.ui.chat.ChatFragment
import edu.bu.metcs673.project.ui.listener.ChatRoomClickListener
import edu.bu.metcs673.project.ui.login.LogInActivity


// @class MainActivity
// @brief Class to handle main configurations for chat application.
//      Instantiated upon application login.

class MainActivity : BaseActivity(), ChatRoomClickListener {

    // Global variable namespace
    companion object {
        private const val TAG = "MainActivity"
    }

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_chat,
                R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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
            val myApp = (application as ICApp)
            myApp.getGoogleSignInClient().signOut()
            myApp.currentUser = null

            //Navigate Back to Sign In Screen
            val logoutIntent = Intent(this, LogInActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun chatRoomClick(chatRoomModel: ChatRoomModel) {
        val chatActivityIntent = Intent(this, ChatActivity::class.java)
        chatActivityIntent.putExtra("CHATROOM_ID", chatRoomModel.chatRoomId)
        if (chatRoomModel.users != null) {
            val users = chatRoomModel.users as ArrayList<User>
            for (user in users) {
                if (user.userId != FirebaseAuth.getInstance().currentUser?.uid) {
                    chatActivityIntent.putExtra("CHATROOM_NAME", user.name)
                    break
                }
            }
        }
        startActivity(chatActivityIntent)
    }

}