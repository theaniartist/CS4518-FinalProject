package com.group18.android.reminderapplication

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.group18.android.reminderapplication.database.EventDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "event-database"

class EventRepository private constructor(context: Context) {

    private val database : EventDatabase = Room.databaseBuilder(
        context.applicationContext,
        EventDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val eventDao = database.eventDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getEvents(): LiveData<List<Event>> = eventDao.getEvents()

    fun getEvent(id: UUID): LiveData<Event?> = eventDao.getEvent(id)


    fun updateEvent(event: Event) {
        executor.execute {eventDao.updateEvent(event)
        }
    }

    fun addEvent(event: Event) {
        executor.execute {eventDao.addEvent(event)}
    }

    companion object {
        private var INSTANCE: EventRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = EventRepository(context)
            }
        }
        fun get(): EventRepository {
            return INSTANCE ?:
            throw IllegalStateException("EventRepository must be initialized")
        }
    }
}