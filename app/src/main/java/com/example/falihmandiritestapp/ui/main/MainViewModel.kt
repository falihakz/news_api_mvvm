package com.example.falihmandiritestapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.falihmandiritestapp.common.SingleLiveEvent
import com.example.falihmandiritestapp.data.entity.Article
import com.example.falihmandiritestapp.data.repository.ArticleRepository
import com.example.falihmandiritestapp.model.FilterBundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainViewModel(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _loadingArticleListEvent = MutableLiveData<Boolean>()
    val loadingArticleListEvent: LiveData<Boolean> = _loadingArticleListEvent
    private val _articleSearchResults = articleRepository.articleSearchResults
    val articleSearchResults: LiveData<List<Article>> = _articleSearchResults
    // untuk saat ini kacangin dulu detail errornya. Pake boolean aja dulu.
    private val _articleSearchFetchError = MutableLiveData<Boolean>()
    val articleSearchFetchError: LiveData<Boolean> = _articleSearchFetchError
    val listEmpty: LiveData<Boolean> = Transformations.map(_articleSearchResults){
        it.isNullOrEmpty() && mPage > 1
    }
    // for data filter and pagination
    var mPage = 1
        private set
    val openArticleDetailEvent = SingleLiveEvent<String>()
    private val mFilter = SingleLiveEvent<FilterBundle>()

    // for calling APIs
    private val disposables = CompositeDisposable()

    private fun retrieveTopHeadline(page: Int, keyword: String, category: String, source: String) {
        val observable = articleRepository.fetchArticle(page, keyword, category, source)

        val disposable = observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _loadingArticleListEvent.postValue(true)
                _articleSearchFetchError.postValue(false)
            }
            .doOnDispose {
                _loadingArticleListEvent.postValue(false)
                _articleSearchFetchError.postValue(false)
            }.doOnComplete {
                _loadingArticleListEvent.postValue(false)
            }.subscribe({
                if(it.isSuccessful){
                    val combinedList: List<Article> = if(page ==1)
                        it.body()?.articles ?: listOf()
                    else
                        _articleSearchResults.value?.toTypedArray()?.let { it1 ->
                            listOf(*it1, *it.body()?.articles?.toTypedArray() ?: listOf<Article>().toTypedArray()) }
                            ?: listOf(*it.body()?.articles?.toTypedArray() ?: listOf<Article>().toTypedArray())
                    _articleSearchResults.postValue(combinedList)

                    // todo kalau sempet: simpen hasil pencarian terakhir sbg cache untuk jaga2
                    //  kalau internet down jadi bisa dicari lagi hasil pencarian terakhir
                    articleRepository.insertAll(it.body()?.articles)
                    mPage = page + 1
//              } else _articleSearchFetchError.postValue(it.errorBody().toString())
                } else _articleSearchFetchError.postValue(true)
            }, {
                _loadingArticleListEvent.postValue(false)
//              _articleSearchFetchError.postValue(it.message.toString())
                _articleSearchFetchError.postValue(true)
            })

        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    fun loadNextPage(){
        mFilter.value?.let {
            retrieveTopHeadline(mPage, it.keyword, it.category, it.source)
        }
    }

    fun applyFilter(it: FilterBundle) {
        // reset pagination
        mPage = 1
        // clear error flag
        _articleSearchFetchError.postValue(false)
        // clear previous list
        _articleSearchResults.postValue(listOf())
        // call API
        mFilter.value = it.also {
            retrieveTopHeadline(mPage, it.keyword, it.category, it.source)
        }
    }

    fun getArticleThumbnailUrlAt(position: Int): String = articleSearchResults.value?.get(position)?.urlToImage?:""
    fun getArticleTitleAt(position: Int): String = articleSearchResults.value?.get(position)?.title?:""
    fun getArticlePublishedTimeAt(position: Int): String {
        val publishedDate: Date = articleSearchResults.value?.get(position)?.publishedAt?: Date()
        val milliseconds = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time.time - publishedDate.time
        val minutes = milliseconds / (1000 * 60)
        if (minutes < 60) return "${minutes}m ago"
        val hours = minutes / 60
        if (hours < 24) return "${hours}h ago"
        return "${hours / 24}d ago"
    }
    fun getArticleSourceAt(position: Int): String = articleSearchResults.value?.get(position)?.source?.name ?:""
    fun getArticleDescriptionAt(position: Int): String = articleSearchResults.value?.get(position)?.description ?:""
    fun openArticleDetailAt(position: Int){
        openArticleDetailEvent.postValue(articleSearchResults.value?.get(position)?.url)
    }
}
