package com.example.falihmandiritestapp.di.module

import android.content.Context
import com.example.falihmandiritestapp.common.adapter.BindableAdapter
import com.example.falihmandiritestapp.data.entity.Article
import com.example.falihmandiritestapp.ui.main.MainViewModel

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return context
    }
}
