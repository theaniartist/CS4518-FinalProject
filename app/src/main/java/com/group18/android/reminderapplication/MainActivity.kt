package com.group18.android.reminderapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), EventListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_main)
        if (currentFragment == null) {
            val fragment = EventListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_main, fragment)
                .commit()
        }
    }

    override fun onEventSelected(eventId: UUID) {
        val fragment = EventFragment.newInstance(eventId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_main, fragment)
            .addToBackStack(null)
            .commit()
    }
}