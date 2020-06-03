package com.example.falihmandiritestapp.modules

import android.content.Context
import com.example.falihmandiritestapp.api.APIServices
import com.example.falihmandiritestapp.data.repository.ArticleRepository
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
}