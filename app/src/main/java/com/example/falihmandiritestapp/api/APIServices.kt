package com.example.falihmandiritestapp.api

import com.example.falihmandiritestapp.common.NEWS_API_TOP_HEADLINES
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServices {

    @GET(NEWS_API_TOP_HEADLINES)
    fun getTopHeadLines(
        @Query("category") category: String,
        @Query("sources") sources: String,
        @Query("q", encoded = true) q: String,
        @Query("pageSize") perPage: Int = 10,
        @Query("page") page: Int = 1
    ): Call<ArticleResponse>

}