package com.example.falihmandiritestapp.di.component

import com.example.falihmandiritestapp.data.repository.ArticleRepository
import com.example.falihmandiritestapp.data.repository.CategoryRepository
import com.example.falihmandiritestapp.data.repository.SourceRepository
import com.example.falihmandiritestapp.di.module.ApiGeneratorModule
import com.example.falihmandiritestapp.di.module.AppModule
import com.example.falihmandiritestapp.di.module.RepositoryModules
import com.example.falihmandiritestapp.ui.filter.FilterFragment
import com.example.falihmandiritestapp.ui.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ApiGeneratorModule::class, RepositoryModules::class])

@Singleton
interface AppComponent {
    fun getArticleRepository(): ArticleRepository
    fun getCategoryRepository(): CategoryRepository
    fun getSourceRepository(): SourceRepository
}
