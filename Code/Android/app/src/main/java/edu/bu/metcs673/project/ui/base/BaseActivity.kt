package edu.bu.metcs673.project.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.bu.metcs673.project.api.UserApi
import edu.bu.metcs673.project.core.ICApp
import retrofit2.Retrofit
import javax.inject.Inject

open class BaseActivity: AppCompatActivity() {
    @Inject lateinit var retrofit: Retrofit

    lateinit var userApi: UserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as ICApp).getNetworkComponent().inject(this)

        userApi = retrofit.create(UserApi::class.java);
    }
}