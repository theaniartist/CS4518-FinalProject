package com.group18.android.reminderapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.group18.android.reminderapplication.model.Event
import java.util.*

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun getEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE id=(:id)")
    fun getEvent(id: UUID): LiveData<Event?>

    @Update
    fun updateEvent(event: Event)

    @Insert
    fun addEvent(event: Event)
}