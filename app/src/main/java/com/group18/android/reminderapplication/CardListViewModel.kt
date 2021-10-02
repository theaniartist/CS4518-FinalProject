package com.group18.android.reminderapplication

import androidx.lifecycle.ViewModel

class CardListViewModel : ViewModel() {

    private val cardRepository = CardRepository.get()
    val cardListLiveData = cardRepository.getCards()

    init {
        // Uncomment to generate a new set of dummy data
        generateDataset()
    }

    private fun generateDataset() {
        for (i in 1..100) {
            val titleName = generateName()
            val desc = generateDescName()

            cardRepository.addCard(Card(title = "Title $titleName", desc = "Description $desc"))
        }
    }

    private fun generateName(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..8).map {
            kotlin.random.Random.nextInt(0, charPool.size)
        }.map(charPool::get).joinToString("")
    }

    private fun generateDescName(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..8).map {
            kotlin.random.Random.nextInt(0, charPool.size)
        }.map(charPool::get).joinToString("")
    }
}