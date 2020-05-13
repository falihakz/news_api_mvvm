package com.example.falihmandiritestapp.data.entity

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int? = null,
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: Date? = null,
    var content: String? = null,
    @Ignore
    var source: Source? = null
)
