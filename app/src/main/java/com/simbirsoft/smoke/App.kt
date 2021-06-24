package com.simbirsoft.smoke

import android.app.Application
import com.simbirsoft.smoke.di.*

class App : Application() {
    lateinit var appComponent: AppComponent
    var hookahComponent: HookahComponent? = null
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        hookahComponent =
            DaggerHookahComponent.builder().appComponent(appComponent).hookahModule(HookahModule()).build()
    }
}