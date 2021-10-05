package com.group18.android.reminderapplication

import androidx.lifecycle.ViewModel

class CardListViewModel : ViewModel() {

    private val cardRepository = CardRepository.get()
    val cardListLiveData = cardRepository.getCards()

    //Only using this dummy database for now before switching over to non-relational DB
    init {
        // Uncomment to generate a new set of dummy data
        //generateDataset()
    }

    private fun generateDataset() {
        val arrayTitle = arrayOf("Happy Birthday Card", "Happy Anniversary Card",
            "Graduation Card", "Christmas Card", "Happy New Year's Card", "Happy Mother's Day Card",
            "Happy Father's Day Card", "Valentine's Day Card", "Wedding Card", "Halloween Card")

        for (i in arrayTitle.indices) {
            val titleName = arrayTitle[i]
            val desc = generateDesc(titleName)
            cardRepository.addCard(Card(title = titleName, desc = "Description: $desc"))
        }
    }

    private fun generateDesc(titleName: String): String {
        return when (titleName) {
            "Happy Birthday Card" -> {"Celebrate someone's Birthday"}
            "Happy Anniversary Card" -> {"Celebrate you and your partner's special day"}
            "Graduation Card" -> {"Congratulate someone on their achievements"}
            "Christmas Card" -> {"Wish someone a Merry Christmas"}
            "Happy New Year's Card" -> {"Celebrate the New Year with someone"}
            "Happy Mother's Day Card" -> {"Celebrate your mother on this special day"}
            "Happy Father's Day Card" -> {"Celebrate your father on this special day"}
            "Valentine's Day Card" -> {"Celebrate this special day with people you love"}
            "Wedding Card" -> {"Celebrate someone's wedding day"}
            else -> {"Spooky scary skeletons"}
        }
    }
}