package com.group18.android.reminderapplication.model

class Message {
	var text: String? = null
	var name: String? = null
	var photoUrl: String? = null
	var imageUrl: String? = null

	// Empty constructor needed for Firebase serialization
	constructor()

	constructor(text: String?, name: String?, photoUrl: String?, imageUrl: String?) {
		this.text = text
		this.name = name
		this.photoUrl = photoUrl
		this.imageUrl = imageUrl
	}
}