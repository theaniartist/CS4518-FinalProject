package com.group18.android.reminderapplication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.group18.android.reminderapplication.model.Card
import java.io.File
import java.util.*

private const val TAG = "GameViewModel"

// TODO: All this needs to be changed to work with Firebase

class CardViewModel : ViewModel() {
//    private val cardRepository = CardRepository.get()
    private val cardIdLiveData = MutableLiveData<UUID>()

//    var cardLiveData: LiveData<Card?> = Transformations.switchMap(cardIdLiveData) { cardId ->
//        cardRepository.getCard(cardId)
//    }
//
//    init {
//        Log.d(TAG, "ViewModel instance created")
//    }
//
//    fun loadCard(cardId: UUID) {
//        cardIdLiveData.value = cardId
//    }
//
//    fun saveCard(card: Card) {
//        cardRepository.updateCard(card)
//    }
//
//    fun getPhotoFile(card: Card): File {
//        return cardRepository.getPhotoFile(card)
//    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}