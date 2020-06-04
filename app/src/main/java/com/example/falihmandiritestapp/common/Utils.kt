package com.example.falihmandiritestapp.common

import java.util.*

fun Date.toTimeSpanString(): String{
    val nowInMilliseconds = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time.time
    if (nowInMilliseconds < this.time)
        return "This date is from the future"
    val timeDiff = nowInMilliseconds - this.time
    val minutes = timeDiff / (1000 * 60)
    if (minutes < 60) return "${minutes}m ago"
    val hours = minutes / 60
    if (hours < 24) return "${hours}h ago"
    val days = hours / 24
    if (days < 30) return "${days}d ago"
    return "${days / 30}Mo ago"
}