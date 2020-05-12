package com.example.falihmandiritestapp.api

import com.example.falihmandiritestapp.common.NEWS_API_SOURCES
import com.example.falihmandiritestapp.common.NEWS_API_TOP_HEADLINES
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServices {

    @GET(NEWS_API_TOP_HEADLINES)
    fun getTopHeadLines(
        @Query("category") category: String,
        @Query("sources") sources: String,
        @Query("q", encoded = true) q: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") perPage: Int = 10
    ): Observable<Response<ArticleResponse>>

    @GET(NEWS_API_SOURCES)
    fun getSources(
        @Query("category") category: String,
        @Query("language") language: String = "en"
    ): Observable<Response<SourceResponse>>
}