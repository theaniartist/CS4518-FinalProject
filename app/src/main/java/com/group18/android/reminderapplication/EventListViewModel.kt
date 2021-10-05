package com.group18.android.reminderapplication

import androidx.lifecycle.ViewModel

class EventListViewModel :ViewModel() {
    val events = mutableListOf<Event>()
    init {
        for (i in 0 until 100) {
            val event = Event()
            event.title = "Event #$i"
            events += event
        }
    }

}