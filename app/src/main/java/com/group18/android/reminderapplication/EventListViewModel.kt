package com.group18.android.reminderapplication

import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class EventListViewModel :ViewModel() {

    private val eventRepository = EventRepository.get()
    val eventListLiveData = eventRepository.getEvents()

    init {
        // Uncomment to generate a new set of dummy data
        //generateDataset()
    }

    private fun generateDataset() {

        for (i in 1..10) {
            val titleName = generateTitle()
            val date = generateDate()
            eventRepository.addEvent(Event(title = titleName, date = date))
        }
    }

    private fun generateTitle(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..8).map {
            kotlin.random.Random.nextInt(0, charPool.size)
        }.map(charPool::get).joinToString("")
    }

    private fun generateDate(): Date {
        val randomMonth = kotlin.random.Random.nextInt(1, 12)
        val randomDate = kotlin.random.Random.nextInt(1, 31)
        val randomYear = kotlin.random.Random.nextInt(2021, 2030)
        val randomHour = kotlin.random.Random.nextInt(0, 23)
        val randomMin = kotlin.random.Random.nextInt(0,59)
        val randomSec = kotlin.random.Random.nextInt(0,59)
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val strDate = "$randomMonth ${randomDate}, $randomYear"
        //val dateTime = LocalDateTime.now()
        //val date = dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        val formatter = DateTimeFormatter.ofPattern("M d, yyyy", Locale.ENGLISH)
        val localDate = LocalDate.parse(strDate, formatter)
        val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

        return date
    }
}