package com.group18.android.reminderapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

class SubActivity : AppCompatActivity(), CardListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_sub)
        if (currentFragment == null) {
            val fragment = CardListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_sub, fragment)
                .commit()
        }
    }

    override fun onCardSelected(cardId: UUID) {

        val email = intent.getStringExtra(EXTRA_EMAIL)!!

        val fragment = CardFragment.newInstance(cardId, email)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_sub, fragment)
            .addToBackStack(null)
            .commit()
    }
    companion object {
        private const val EXTRA_EMAIL = "com.group18.android.reminderapplication.email"
    }
}