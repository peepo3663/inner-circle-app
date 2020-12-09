package edu.bu.metcs673.project.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.HasAndroidInjector
import edu.bu.metcs673.project.api.ChatApi
import edu.bu.metcs673.project.api.MessageAPI
import edu.bu.metcs673.project.api.UserApi
import edu.bu.metcs673.project.core.ICApp
import retrofit2.Retrofit
import javax.inject.Inject

open class BaseFragment: Fragment() {

    //    @Inject lateinit var androidInjector : DispatchingAndroidInjector<Any>
    @Inject lateinit var retrofit: Retrofit
    lateinit var userApi: UserApi
    lateinit var messageAPI: MessageAPI
    lateinit var chatApi: ChatApi

    val currentUserId: String? = com.google.firebase.auth.FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.application?.let {
            val app = it as ICApp
            app.getNetworkComponent().inject(this)
        }

        userApi = retrofit.create(UserApi::class.java)
        messageAPI = retrofit.create(MessageAPI::class.java)
        chatApi = retrofit.create(ChatApi::class.java)
    }

    protected fun showLoader(showLoader: Boolean) {
        val currentActivity = activity as BaseActivity?
        currentActivity?.let {
            it.showPopupProgressSpinner(showLoader)
        }
    }
}