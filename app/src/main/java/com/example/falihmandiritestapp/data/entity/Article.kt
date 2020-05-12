package com.example.falihmandiritestapp.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "articles",
    indices=[Index("url", unique = true), Index("publishedAt")])
class Article {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var id: Int? = null
    var author: String? = null
    var title: String? = null
    var description: String? = null
    var url: String? = null
    @ColumnInfo(name = "url_to_image")
    var urlToImage: String? = null
    @ColumnInfo(name = "published_at")
    var publishedAt: Date? = null
    var publisher: String? = null
}
