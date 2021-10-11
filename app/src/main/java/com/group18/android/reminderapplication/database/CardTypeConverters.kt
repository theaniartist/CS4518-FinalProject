package com.group18.android.reminderapplication.database

import androidx.room.TypeConverter
import java.util.*

class CardTypeConverters {
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}