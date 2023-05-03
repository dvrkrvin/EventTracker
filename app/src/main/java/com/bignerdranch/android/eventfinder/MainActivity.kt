package com.bignerdranch.android.eventfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.eventfinder.ui.main.EventsListFragment

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EventsListFragment.newInstance())
                .commitNow()
        }
        setSupportActionBar(findViewById(R.id.my_toolbar))
        window.statusBarColor = getColor(R.color.dark_blue)
    }
}