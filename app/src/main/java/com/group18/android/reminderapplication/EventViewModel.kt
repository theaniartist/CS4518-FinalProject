package com.group18.android.reminderapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class EventViewModel: ViewModel() {

    private val eventRepository = EventRepository.get()
    private val eventIdLiveData = MutableLiveData<UUID>()
    var eventLiveData: LiveData<Event?> =
        Transformations.switchMap(eventIdLiveData) { eventId ->
            eventRepository.getEvent(eventId)
        }

    fun addEvent(event: Event) {
        eventRepository.addEvent(event)
    }
}