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

    fun addEvent(event: Event) {
        eventRepository.addEvent(event)
    }

    private fun generateDataset() {
        val arrayTitle = arrayOf("Birthday", "Anniversary",
            "Graduation", "Christmas", "New Year's", "Mother's Day",
            "Father's Day", "Valentine's Day", "Wedding", "Halloween")

        for (i in arrayTitle.indices) {
            val titleName = arrayTitle[i]
            val date = generateDate(titleName)
            eventRepository.addEvent(Event(title = titleName, email="", date = date))
        }
    }

    private fun generateDate(title: String): Date {
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val randomMonth = kotlin.random.Random.nextInt(1, 12)
        val randomDate = kotlin.random.Random.nextInt(1, 31)
        val randomYear = kotlin.random.Random.nextInt(2021, 2022)
        //Not sure if we need time, probably look for a way to implement that if we want it
        val randomHour = kotlin.random.Random.nextInt(0, 23)
        val randomMin = kotlin.random.Random.nextInt(0, 59)
        val randomSec = kotlin.random.Random.nextInt(0, 59)

        when (title) {
            "Valentine's Day" -> {
                val specificDate = "2 14, $randomYear"
                val formatter = DateTimeFormatter.ofPattern("M d, yyyy", Locale.ENGLISH)
                val localDate = LocalDate.parse(specificDate, formatter)
                val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                return date
            }
            "Christmas" -> {
                val specificDate = "12 25, $randomYear"
                val formatter = DateTimeFormatter.ofPattern("M d, yyyy", Locale.ENGLISH)
                val localDate = LocalDate.parse(specificDate, formatter)
                val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                return date
            }
            "New Year's" -> {
                val specificDate = "1 1, $randomYear"
                val formatter = DateTimeFormatter.ofPattern("M d, yyyy", Locale.ENGLISH)
                val localDate = LocalDate.parse(specificDate, formatter)
                val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                return date
            }
            "Halloween" -> {
                val specificDate = "10 31, $randomYear"
                val formatter = DateTimeFormatter.ofPattern("M d, yyyy", Locale.ENGLISH)
                val localDate = LocalDate.parse(specificDate, formatter)
                val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                return date
            }
            "Mother's Day" -> {
                val rand = kotlin.random.Random.nextInt(8, 13)
                val specificDate = "5 $rand, $randomYear"
                val formatter = DateTimeFormatter.ofPattern("M d, yyyy", Locale.ENGLISH)
                val localDate = LocalDate.parse(specificDate, formatter)
                val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                return date
            }
            "Father's Day" -> {
                val rand = kotlin.random.Random.nextInt(6, 21)
                val specificDate = "6 $rand, $randomYear"
                val formatter = DateTimeFormatter.ofPattern("M d, yyyy", Locale.ENGLISH)
                val localDate = LocalDate.parse(specificDate, formatter)
                val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                return date
            }
            "Graduation" -> {
                val specificDate = "5 14, 2022"
                val formatter = DateTimeFormatter.ofPattern("M d, yyyy", Locale.ENGLISH)
                val localDate = LocalDate.parse(specificDate, formatter)
                val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                return date
            }
        }

        val strDate = "$randomMonth ${randomDate}, $randomYear"
        //val dateTime = LocalDateTime.now()
        //val date = dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        val formatter = DateTimeFormatter.ofPattern("M d, yyyy", Locale.ENGLISH)
        val localDate = LocalDate.parse(strDate, formatter)
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())
    }
}