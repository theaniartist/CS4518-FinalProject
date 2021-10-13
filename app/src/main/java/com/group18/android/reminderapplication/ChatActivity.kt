package com.group18.android.reminderapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.group18.android.reminderapplication.databinding.ActivityChatBinding
import com.group18.android.reminderapplication.model.Message
import java.io.File

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var manager: LinearLayoutManager

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter
    private lateinit var messagesRef: DatabaseReference

//    private val openDocument = registerForActivityResult(OpenDocumentContract()) { uri ->
//        onImageSelected(uri)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        if (auth.currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }

        db = Firebase.database

        val senderEmail = auth.currentUser!!.email!!
        val recipientEmail = intent.getStringExtra(EXTRA_EMAIL)!!
        val email1 = if (senderEmail < recipientEmail) senderEmail else recipientEmail
        val email2 = if (email1 == senderEmail) recipientEmail else senderEmail
        val chatRoomId = ("[$email1]/[$email2]").hashCode().toString()
        messagesRef = db.reference.child(MESSAGES_CHILD).child(chatRoomId)

        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(messagesRef, Message::class.java)
            .build()
        adapter = MessageAdapter(options, getUserName())
        binding.progressBar.visibility = ProgressBar.INVISIBLE
        manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager
        binding.messageRecyclerView.adapter = adapter

        adapter.registerAdapterDataObserver(
            ScrollToBottomObserver(binding.messageRecyclerView, adapter, manager)
        )

        binding.messageEditText.addTextChangedListener(ButtonObserver(binding.sendButton))

        binding.sendButton.setOnClickListener {
            val message = Message(
                binding.messageEditText.text.toString(),
                getUserName(),
                getPhotoUrl(),
                null
            )

            messagesRef.push().setValue(message)
            binding.messageEditText.setText("")
        }

        val cardPath = intent.getStringExtra(EXTRA_TEMPLATE)
        if (cardPath != null) {
            val cardUri = Uri.parse(cardPath)
            Log.d(TAG, "card path: $cardPath")
            onImageSelected(cardUri)
        }

        val photoPath = intent.getStringExtra(EXTRA_PHOTO)
        if (photoPath != null) {
            val photoUri = Uri.fromFile(File(photoPath))
            Log.d(TAG, "photo path: $photoPath")
            onImageSelected(photoUri)
        }
    }

    public override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
            return
        }
    }

    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onImageSelected(uri: Uri) {
        Log.d(TAG, "Uri: $uri")
        val user = auth.currentUser
        val tempMessage = Message(null, getUserName(), getPhotoUrl(), LOADING_IMAGE_URL)

        messagesRef
            .push()
            .setValue(
                tempMessage,
                DatabaseReference.CompletionListener { databaseError, databaseReference ->
                    if (databaseError != null) {
                        val ex = databaseError.toException()
                        Log.w(TAG, "Unable to write message to database.", ex)
                        return@CompletionListener
                    }

                    // Build a StorageReference and then upload the file
                    val key = databaseReference.key
                    val storageReference = Firebase.storage.getReference(user!!.uid).child(key!!).child(uri.lastPathSegment!!)
                    putImageInStorage(storageReference, uri, key)
                })
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
        storageReference.putFile(uri).addOnSuccessListener(this) { taskSnapshot ->
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    val message = Message(null, getUserName(), getPhotoUrl(), uri.toString())
                    messagesRef.child(key!!).setValue(message)
                }
        }
            .addOnFailureListener(this) { ex ->
                Log.w(TAG, "Image upload task was unsuccessful.", ex)
            }
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(this)
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun getPhotoUrl(): String? {
        val user = auth.currentUser
        return user?.photoUrl?.toString()
    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) user.displayName else ANONYMOUS
    }

    companion object {
        private const val TAG = "ChatActivity"
        const val MESSAGES_CHILD = "messages"
        const val ANONYMOUS = "anonymous"
        private const val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"
        private const val EXTRA_PHOTO = "com.group18.android.reminderapplication.photo"
        private const val EXTRA_TEMPLATE = "com.group18.android.reminderapplication.template"
        private const val EXTRA_EMAIL = "com.group18.android.reminderapplication.email"
    }
}