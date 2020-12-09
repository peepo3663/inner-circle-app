package edu.bu.metcs673.project.dagger

import dagger.Component
import edu.bu.metcs673.project.module.AppModule
import edu.bu.metcs673.project.module.NetworkModule
import edu.bu.metcs673.project.ui.base.BaseActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(baseActivity: BaseActivity)
}