package com.group18.android.reminderapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Event (@PrimaryKey val id: UUID = UUID.randomUUID(),
                  var title: String = "",
                  var email: String = "",
                  var date: Date = Date())
