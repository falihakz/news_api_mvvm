package com.example.falihmandiritestapp.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.falihmandiritestapp.common.SingleLiveEvent
import com.example.falihmandiritestapp.data.entity.Source
import com.example.falihmandiritestapp.data.repository.CategoryRepository
import com.example.falihmandiritestapp.data.repository.SourceRepository
import com.example.falihmandiritestapp.model.FilterBundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FilterViewModel(
    private val categoryRepository: CategoryRepository,
    private val sourceRepository: SourceRepository
) : ViewModel() {

    private val _selectedCategory = MutableLiveData<String>()
    val selectedCategory: LiveData<String> = _selectedCategory
    private val _selectedSource = MutableLiveData<String>()
    val selectedSource: LiveData<String> = _selectedSource
    private val _articleSources = MutableLiveData<List<Source>>()
    val articleSources: LiveData<List<Source>> = _articleSources
    private val mKeyword = MutableLiveData<String?>()
    private val mKeywordObserver = Observer<String?> {

        eFilter.postValue(FilterBundle(
            keyword = it?:"",
            category = selectedCategory.value?:"",
            source = selectedSource.value?:""))
    }

    private val selectedCategoryObserver = Observer<String?> {
        // reset selected source
        _selectedSource.value = ""
        retrieveSources(it?:"")
        setKeyword(mKeyword.value?:"")
    }
    private val _loadingSourceListEvent = MutableLiveData<Boolean>()
    val loadingSourceListEvent: LiveData<Boolean> = _loadingSourceListEvent
    private val _sourcesFetchError = MutableLiveData<Boolean>()
    val sourcesFetchError: LiveData<Boolean> = _sourcesFetchError
    val eFilter = SingleLiveEvent<FilterBundle>()

    // for calling APIs
    private val disposables = CompositeDisposable()

    init {
        mKeyword.observeForever(mKeywordObserver)
        _selectedCategory.observeForever(selectedCategoryObserver)
        setCategory("General")
    }

    fun setKeyword(keyword: String){
        mKeyword.postValue(keyword)
    }

    fun resetSearch() {
        setKeyword("")
    }

    fun setCategory(categoryString: String) {
        _selectedCategory.value = categoryString
        setKeyword(mKeyword.value?:"")
    }

    fun setSource(sourceString: String) {
        _selectedSource.value = sourceString
        setKeyword(mKeyword.value?:"")
    }

    private fun retrieveSources(category: String){
        val observable = sourceRepository.fetchSources(category)

        val disposable = observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                _loadingSourceListEvent.postValue(true)
                _sourcesFetchError.postValue(false)
            }
            .doOnDispose {
                _loadingSourceListEvent.postValue(false)
                _sourcesFetchError.postValue(false)
            }.subscribe({
                if(it.isSuccessful){
                    _loadingSourceListEvent.postValue(false)
                    _articleSources.postValue(it.body()?.sources)
                } else _sourcesFetchError.postValue(true)
            }, {
                _loadingSourceListEvent.postValue(false)
                _sourcesFetchError.postValue(true)
            })

        disposables.add(disposable)
    }

    fun getAllCategories(): Array<String> {
        return categoryRepository.getAllCategories()
    }

    override fun onCleared() {
        disposables.dispose()
        mKeyword.removeObserver(mKeywordObserver)
        _selectedCategory.removeObserver(selectedCategoryObserver)
        super.onCleared()
    }
}
