package com.example.falihmandiritestapp.data.repository

import android.content.Context
import com.example.falihmandiritestapp.R

class CategoryRepository(
    private val context: Context
) {
    fun getAllCategories(): Array<String> = context.resources.getStringArray(R.array.categories)
}