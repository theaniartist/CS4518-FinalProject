package com.group18.android.reminderapplication

import java.util.*

data class Event (val id: UUID = UUID.randomUUID(),
                  var title: String = "",
                  var date: Date = Date())
