package com.example.falihmandiritestapp.modules

import android.content.Context
import com.example.falihmandiritestapp.api.APIServices
import com.example.falihmandiritestapp.data.repository.ArticleRepository
import com.example.falihmandiritestapp.data.repository.CategoryRepository
import com.example.falihmandiritestapp.data.repository.SourceRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModules {

    @Provides
    @Singleton
    internal fun provideArticleRepository(context: Context, apiServices: APIServices): ArticleRepository {
        return ArticleRepository(context, apiServices)
    }

    @Provides
    @Singleton
    internal fun provideSourceRepository(context: Context, apiServices: APIServices): SourceRepository {
        return SourceRepository(context, apiServices)
    }

    @Provides
    @Singleton
    internal fun provideCategoryRepository(context: Context): CategoryRepository {
        return CategoryRepository(context)
    }
}