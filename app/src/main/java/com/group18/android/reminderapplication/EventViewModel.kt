package com.group18.android.reminderapplication

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

    fun loadEvent(eventId: UUID) {
        eventIdLiveData.value = eventId
    }

    fun saveEvent(event: Event) {
        eventRepository.updateEvent(event)
    }
}