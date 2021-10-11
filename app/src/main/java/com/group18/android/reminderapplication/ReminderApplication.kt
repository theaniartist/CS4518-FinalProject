package com.group18.android.reminderapplication

import android.app.Application

class ReminderApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		CardRepository.initialize(this)
	}
}