package com.example.falihmandiritestapp.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.falihmandiritestapp.data.repository.CategoryRepository
import com.example.falihmandiritestapp.data.repository.SourceRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FilterViewModelFactory @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val sourceRepository: SourceRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            return FilterViewModel(
                categoryRepository,
                sourceRepository
            ) as T
        }
        throw IllegalArgumentException("invalid view model")
    }

}