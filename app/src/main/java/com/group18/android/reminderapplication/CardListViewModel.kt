package com.group18.android.reminderapplication

import androidx.lifecycle.ViewModel

class CardListViewModel : ViewModel() {

    val cards = mutableListOf<Card>()

    init {
        for (i in 0 until 100) {
            val card = Card()
            card.title = "Card #$i"
            cards += card
        }
    }
}