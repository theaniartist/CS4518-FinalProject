package com.group18.android.reminderapplication

import android.app.Application

class IntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CardRepository.initialize(this)
        EventRepository.initialize(this)
    }
}