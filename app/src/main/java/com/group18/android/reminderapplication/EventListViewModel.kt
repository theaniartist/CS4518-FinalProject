package com.group18.android.reminderapplication

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.group18.android.reminderapplication.model.Event
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

    @RequiresApi(Build.VERSION_CODES.O)
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

    @SuppressLint("NewApi")
    private fun generateDate(title: String): Date {
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val randomMonth = kotlin.random.Random.nextInt(1, 12)
        val randomDate = kotlin.random.Random.nextInt(1, 31)
        val randomYear = kotlin.random.Random.nextInt(2021, 2022)

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
        val formatter = DateTimeFormatter.ofPattern("M d, yyyy", Locale.ENGLISH)
        val localDate = LocalDate.parse(strDate, formatter)
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())
    }
}