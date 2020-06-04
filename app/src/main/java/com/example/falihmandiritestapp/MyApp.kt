package com.example.falihmandiritestapp

import android.app.Application
import com.example.falihmandiritestapp.di.*
import com.example.falihmandiritestapp.di.component.AppComponent
import com.example.falihmandiritestapp.di.component.DaggerAppComponent
import com.example.falihmandiritestapp.di.module.ApiGeneratorModule
import com.example.falihmandiritestapp.di.module.AppModule

class MyApp : Application() {

    companion object {
        /*
         * Singleton pattern for Application.
         * to refer to this object, use:
         * MyApp.getInstance()
         *
         * in alternative, you could also use:
         * (application as MyApp) or (getApplication() as MyApp)
         */
        private lateinit var mInstance: MyApp

        // Getter for singleton MyApp
        @Synchronized
        fun getInstance(): MyApp {
            return mInstance
        }
    }

    // for Dagger use
    var appComponent: AppComponent? = null
        internal set

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        // initialize Dagger components
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .apiGeneratorModule(ApiGeneratorModule())
            .build()
    }
}