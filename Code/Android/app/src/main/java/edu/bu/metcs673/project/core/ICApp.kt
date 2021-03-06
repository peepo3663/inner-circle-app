package edu.bu.metcs673.project.core

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import edu.bu.metcs673.project.BuildConfig
import edu.bu.metcs673.project.R
import edu.bu.metcs673.project.dagger.AppComponent
import edu.bu.metcs673.project.dagger.DaggerAppComponent
import edu.bu.metcs673.project.model.user.User
import edu.bu.metcs673.project.module.AppModule
import edu.bu.metcs673.project.module.NetworkModule

class ICApp: Application() {

//    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>
    private lateinit var appComponent: AppComponent
//    private lateinit var serviceComponent: ServiceComponent
    private lateinit var googleSignInClient: GoogleSignInClient

    var currentUser: User? = null

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).networkModule(
            NetworkModule(BuildConfig.SERVER_URL)).build()
//        serviceComponent = DaggerServiceComponent.builder().serviceModule(ServiceModule())

        AppCenter.start(this, "b9dec75a-1701-4887-aa32-fde1d91eb744", Analytics::class.java, Crashes::class.java)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun getNetworkComponent(): AppComponent {
        return appComponent
    }

    fun getGoogleSignInClient(): GoogleSignInClient = googleSignInClient

//    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}