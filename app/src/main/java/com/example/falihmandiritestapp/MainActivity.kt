package com.example.falihmandiritestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.falihmandiritestapp.ui.filter.FilterFragment
import com.example.falihmandiritestapp.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.list_container, MainFragment.newInstance())
                .replace(R.id.filter_container, FilterFragment.newInstance())
                .commitNow()
        }
    }

}
