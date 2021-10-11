package com.group18.android.reminderapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Card (@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var desc: String = "",
                 var message: String = "",
                 var recipient: String = "") {

    val photoFileName
        get() = "IMG_$id.jpg"
}