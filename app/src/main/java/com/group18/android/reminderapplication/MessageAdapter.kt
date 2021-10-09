package com.group18.android.reminderapplication

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.group18.android.reminderapplication.ChatActivity.Companion.ANONYMOUS
import com.group18.android.reminderapplication.databinding.ImageMessageBinding
import com.group18.android.reminderapplication.databinding.MessageBinding
import com.group18.android.reminderapplication.model.Message

class MessageAdapter (
	private val options: FirebaseRecyclerOptions<Message>,
	private val currentUserName: String?
) : FirebaseRecyclerAdapter<Message, ViewHolder>(options) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)

		return if (viewType == VIEW_TYPE_TEXT) {
			val view = inflater.inflate(R.layout.message, parent, false)
			val binding = MessageBinding.bind(view)
			MessageViewHolder(binding)
		} else {
			val view = inflater.inflate(R.layout.image_message, parent, false)
			val binding = ImageMessageBinding.bind(view)
			ImageMessageViewHolder(binding)
		}
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Message) {
		if (options.snapshots[position].text != null) {
			(holder as MessageViewHolder).bind(model)
		} else {
			(holder as ImageMessageViewHolder).bind(model)
		}
	}

	inner class MessageViewHolder(private val binding: MessageBinding) : ViewHolder(binding.root) {
		fun bind(item: Message) {
			binding.messageTextView.text = item.text
			setTextColor(item.name, binding.messageTextView)

			binding.messengerTextView.text = if (item.name == null) ANONYMOUS else item.name
			if (item.photoUrl != null) {
				loadImageIntoView(binding.messengerImageView, item.photoUrl!!)
			} else {
				binding.messengerImageView.setImageResource(R.drawable.ic_account_circle_black_36dp)
			}
		}

		private fun setTextColor(userName: String?, textView: TextView) {
			if (userName != ANONYMOUS && currentUserName == userName && userName != null) {
				textView.setBackgroundResource(R.drawable.rounded_message_blue)
				textView.setTextColor(Color.WHITE)
			} else {
				textView.setBackgroundResource(R.drawable.rounded_message_gray)
				textView.setTextColor(Color.BLACK)
			}
		}
	}

	override fun getItemViewType(position: Int): Int {
		return if (options.snapshots[position].text != null) VIEW_TYPE_TEXT else VIEW_TYPE_IMAGE
	}

	inner class ImageMessageViewHolder(private val binding: ImageMessageBinding) : ViewHolder(binding.root) {
		fun bind(item: Message) {
			loadImageIntoView(binding.messageImageView, item.imageUrl!!)

			binding.messengerTextView.text = if (item.name == null) ANONYMOUS else item.name
			if (item.photoUrl != null) {
				loadImageIntoView(binding.messengerImageView, item.photoUrl!!)
			} else {
				binding.messengerImageView.setImageResource(R.drawable.ic_account_circle_black_36dp)
			}
		}
	}

	private fun loadImageIntoView(view: ImageView, url: String) {
		if (url.startsWith("gs://")) {
			val storageReference = Firebase.storage.getReferenceFromUrl(url)
			storageReference.downloadUrl
				.addOnSuccessListener { uri ->
					val downloadUrl = uri.toString()
					Glide.with(view.context).load(downloadUrl).into(view)
				}
				.addOnFailureListener { ex ->
					Log.w(TAG, "Failed to get download url", ex)
				}
		} else {
			Glide.with(view.context).load(url).into(view)
		}
	}

	companion object {
		const val TAG = "MessageAdapter"
		const val VIEW_TYPE_TEXT = 1
		const val VIEW_TYPE_IMAGE = 2
	}
}