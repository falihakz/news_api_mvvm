package com.example.falihmandiritestapp.data

import androidx.room.TypeConverter
import java.util.*

class EntityFieldConverters {
    @TypeConverter
    fun fromDate(date: Date?) = date?.time

    @TypeConverter
    fun toDate(millis: Long?) = millis?.let { Date(millis) }
}
