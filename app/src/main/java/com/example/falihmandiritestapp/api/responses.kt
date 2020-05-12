package com.example.falihmandiritestapp.api

import com.example.falihmandiritestapp.database.entity.Article

data class ArticleResponse(val totalResults: Int, val articles: List<Article>)