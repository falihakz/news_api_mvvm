package com.example.falihmandiritestapp.api

import com.example.falihmandiritestapp.data.entity.Article

data class ArticleResponse(val totalResults: Int, val articles: List<Article>)