package com.example.falihmandiritestapp.di.component

import com.example.falihmandiritestapp.di.PerFragment
import com.example.falihmandiritestapp.ui.filter.FilterFragment
import com.example.falihmandiritestapp.ui.main.MainFragment
import dagger.Component

@PerFragment
@Component(dependencies = [AppComponent::class], modules = [])
interface FragmentComponent {
    fun inject(mainFragment: MainFragment)
    fun inject(filterFragment: FilterFragment)

    @Component.Builder
    interface Builder {
        fun appComponent(component: AppComponent): Builder
        fun build(): FragmentComponent
    }
}