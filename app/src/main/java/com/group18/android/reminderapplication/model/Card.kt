package com.group18.android.reminderapplication.model

import java.util.*

data class Card (val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var desc: String = "",
                 var message: String = "",
                 var recipient: String = "") {

    val photoFileName
        get() = "IMG_$id.jpg"
}