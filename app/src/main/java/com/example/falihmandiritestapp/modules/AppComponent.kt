package com.example.falihmandiritestapp.modules

import androidx.fragment.app.Fragment
import com.example.falihmandiritestapp.ui.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ApiGeneratorModule::class, RepositoryModules::class])

@Singleton
interface AppComponent {

    fun doInjection(mainFragment: MainFragment)
}
