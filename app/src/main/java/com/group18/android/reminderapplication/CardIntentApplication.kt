package com.group18.android.reminderapplication

import android.app.Application

class CardIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CardRepository.initialize(this)
    }
}