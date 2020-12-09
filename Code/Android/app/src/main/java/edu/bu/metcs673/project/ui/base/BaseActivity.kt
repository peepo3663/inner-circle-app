package edu.bu.metcs673.project.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.api.MessageAPI
import edu.bu.metcs673.project.api.UserApi
import edu.bu.metcs673.project.core.ICApp
import retrofit2.Retrofit
import javax.inject.Inject

open class BaseActivity: AppCompatActivity() {

    @Inject lateinit var retrofit: Retrofit

    private var dialog: Dialog? = null
    lateinit var userApi: UserApi
    lateinit var messageAPI: MessageAPI

    val currentUser: String? = FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as ICApp).getNetworkComponent().inject(this)

        userApi = retrofit.create(UserApi::class.java)
        messageAPI = retrofit.create(MessageAPI::class.java)
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
}