package com.example.falihmandiritestapp.modules

import com.example.falihmandiritestapp.data.repository.ArticleRepository
import org.koin.dsl.module

val articleRepository= module {
    single { ArticleRepository(get(), get()) }
}