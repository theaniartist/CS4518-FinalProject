package com.group18.android.reminderapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.group18.android.reminderapplication.model.Card

@Database(entities = [Card::class], version=1)
@TypeConverters(CardTypeConverters::class)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}