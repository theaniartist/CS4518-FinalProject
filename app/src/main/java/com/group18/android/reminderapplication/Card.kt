package com.group18.android.reminderapplication

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Card (@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var desc: String = "",
                 var recipient: String = "",
                 var message: String = "") {

    val photoFileName
        get() = "IMG_$id.jpg"
}