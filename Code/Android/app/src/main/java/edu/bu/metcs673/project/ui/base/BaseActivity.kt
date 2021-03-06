package edu.bu.metcs673.project.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.api.ChatApi
import edu.bu.metcs673.project.api.MessageAPI
import edu.bu.metcs673.project.api.UserApi
import edu.bu.metcs673.project.core.ICApp
import edu.bu.metcs673.project.model.user.UserDevice
import edu.bu.metcs673.project.util.SharedPreferencesUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

open class BaseActivity: AppCompatActivity() {

    @Inject lateinit var retrofit: Retrofit

    private var dialog: Dialog? = null
    lateinit var userApi: UserApi
    lateinit var messageAPI: MessageAPI
    lateinit var chatApi: ChatApi

    val currentUserId: String? = FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as ICApp).getNetworkComponent().inject(this)

        userApi = retrofit.create(UserApi::class.java)
        messageAPI = retrofit.create(MessageAPI::class.java)
        chatApi = retrofit.create(ChatApi::class.java)
    }

    fun showPopupProgressSpinner(isShowing: Boolean) {
        if (isShowing) {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.popup_progressbar)
            dialog.setCancelable(false);
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val progress = dialog.findViewById<ProgressBar>(R.id.progressBar)
            progress.indeterminateDrawable.setColorFilter(Color.parseColor("#ff6700"), android.graphics.PorterDuff.Mode.MULTIPLY)
            this.dialog = dialog
            dialog.show()
        } else {
            dialog?.dismiss()
        }
    }

    override fun onPause() {
        super.onPause()
        dialog?.dismiss()
    }

    protected fun signOut() {
        // Log out the user
        currentUserId?.let {
            val userDevice = UserDevice(SharedPreferencesUtils.getString(this, SharedPreferencesUtils.FIREBASE_TOKEN))
            userApi.logoutUser(userDevice, it).enqueue(object: Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    SharedPreferencesUtils.remove(this@BaseActivity, SharedPreferencesUtils.FIREBASE_TOKEN)
                }
            })
        }

        FirebaseAuth.getInstance().signOut()
        // sign out from google too
        val myApp = (application as ICApp)
        myApp.getGoogleSignInClient().signOut()
        myApp.currentUser = null
    }

//    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}