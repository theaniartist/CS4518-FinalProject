package com.group18.android.reminderapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.group18.android.reminderapplication.model.Event

@Database(entities = [Event::class], version = 1)
@TypeConverters(EventTypeConverters::class)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
}