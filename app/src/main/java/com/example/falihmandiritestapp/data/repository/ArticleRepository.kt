package com.example.falihmandiritestapp.data.repository

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.example.falihmandiritestapp.api.APIServices
import com.example.falihmandiritestapp.api.ArticleResponse
import com.example.falihmandiritestapp.api.SourceResponse
import com.example.falihmandiritestapp.data.AppDatabase
import com.example.falihmandiritestapp.data.dao.ArticleDao
import com.example.falihmandiritestapp.data.entity.Article
import io.reactivex.Observable
import retrofit2.Response

//todo: learn to cache result from API into local db using NetworkBoundResource livedata
class ArticleRepository(
    private val context: Context,
    private val apiServices: APIServices
) {

    private val articleDao: ArticleDao
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val articleSearchResults = MutableLiveData<List<Article>>()

    init {
        val db = AppDatabase.getDatabase(context)
        articleDao = db!!.articleDao()
    }

    fun fetchArticle(page: Int, keyword: String, category: String, source: String): Observable<Response<ArticleResponse>> {
        val sanitizedKeyword = keyword.trim().replace(" ", "+")
        val sanitizedSource = source.trim().replace(" ", "+")
        var sanitizedCategory = ""
        if (sanitizedSource.isBlank())
            sanitizedCategory = category.trim().replace(" ", "+")

        return apiServices.getTopHeadLines(sanitizedCategory, sanitizedSource, sanitizedKeyword, page)
    }

    fun fetchSources(category: String): Observable<Response<SourceResponse>> {
        val sanitizedCategory = category.trim().replace(" ", "+")
        return apiServices.getSources(sanitizedCategory)
    }

    fun insertAll(articles: List<Article>?) {
        articles?.let {
            insert(*it.toTypedArray())
        }
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    fun insert(vararg article: Article) {
        InsertAsyncTask(articleDao).execute(*article)
    }

    private class InsertAsyncTask internal constructor(private val articleDao: ArticleDao) :
        AsyncTask<Article, Void, Void>() {

        override fun doInBackground(vararg params: Article): Void? {
            articleDao.insert(*params)
            return null
        }
    }
}