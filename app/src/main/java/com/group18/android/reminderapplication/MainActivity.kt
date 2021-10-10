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

class MainActivity : AppCompatActivity(), CardListFragment.Callbacks {
    private lateinit var auth: FirebaseAuth
    private lateinit var chatButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        if (auth.currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }

        chatButton = findViewById(R.id.chat_button)
        chatButton.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
            finish()
        }
        // Not using fragment temporarily to test chat
//        val currentFragment =
//            supportFragmentManager.findFragmentById(R.id.fragment_container)
//        if (currentFragment == null) {
//            val fragment = CardListFragment.newInstance()
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.fragment_container, fragment)
//                .commit()
//        }
    }

    override fun onCardSelected(cardId: UUID) {
        //Log.d(TAG, "MainActivity.onCardSelected: $cardId")
        val fragment = CardFragment.newInstance(cardId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}