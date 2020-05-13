package com.example.falihmandiritestapp.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sources")
class Source {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var id: String? = null
    var name: String? = null
    var description: String? = null
    var url: String? = null
    var category: String? = null
    var language: String? = null
    var country: String? = null
}
