package com.example.falihmandiritestapp

import com.example.falihmandiritestapp.common.toTimeSpanString
import com.example.falihmandiritestapp.data.EntityFieldConverters
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun timeSpanTest() {
        val today = Calendar.getInstance()
        assertEquals("0m ago", today.time.toTimeSpanString())

        today.add(Calendar.MINUTE, -10)
        assertEquals("10m ago", today.time.toTimeSpanString())

        today.add(Calendar.HOUR, -6)
        assertEquals("6h ago", today.time.toTimeSpanString())

        today.add(Calendar.DATE, -3)
        assertEquals("3d ago", today.time.toTimeSpanString())

        today.add(Calendar.MONTH, -8)
        assertEquals("8Mo ago", today.time.toTimeSpanString())

        today.add(Calendar.MONTH, 999999999)
        assertEquals("This date is from the future", today.time.toTimeSpanString())
    }
}
