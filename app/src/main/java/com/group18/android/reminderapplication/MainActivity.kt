package com.group18.android.reminderapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), EventListFragment.Callbacks {
    private lateinit var auth: FirebaseAuth
//    private lateinit var chatButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        if (auth.currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }

//        chatButton = findViewById(R.id.chat_button)
//        chatButton.setOnClickListener {
//            startActivity(Intent(this, ChatActivity::class.java))
//            finish()
//        }
        // Not using fragment temporarily to test chat
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