package com.example.falihmandiritestapp.api

import com.example.falihmandiritestapp.data.entity.Article
import com.example.falihmandiritestapp.data.entity.Source

data class ArticleResponse(val totalResults: Int, val articles: List<Article>)
data class SourceResponse(val status: String, val sources: List<Source>)