package com.example.falihmandiritestapp.data.repository

import android.content.Context
import com.example.falihmandiritestapp.api.APIServices
import com.example.falihmandiritestapp.api.SourceResponse
import io.reactivex.Observable
import retrofit2.Response

class SourceRepository(
    private val context: Context,
    private val apiServices: APIServices
) {

    fun fetchSources(category: String): Observable<Response<SourceResponse>> {
        val sanitizedCategory = category.trim().replace(" ", "+")
        return apiServices.getSources(sanitizedCategory)
    }
}