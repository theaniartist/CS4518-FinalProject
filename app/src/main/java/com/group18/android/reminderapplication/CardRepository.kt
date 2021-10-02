package com.group18.android.reminderapplication

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.group18.android.reminderapplication.database.CardDatabase
import java.io.File
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "card-database"

class CardRepository private constructor(context: Context){

    private val database : CardDatabase = Room.databaseBuilder(
        context.applicationContext,
        CardDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val cardDao = database.cardDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun addCard(card: Card) {
        executor.execute {cardDao.addCard(card)}
    }

    fun updateCard(card: Card) {
        executor.execute {cardDao.updateCard(card)}
    }

    fun getCards(): LiveData<List<Card>> = cardDao.getCards()

    fun getCard(id: UUID): LiveData<Card?> = cardDao.getCard(id)

    fun getPhotoFile(card: Card): File = File(filesDir, card.photoFileName)

    companion object {
        private var INSTANCE: CardRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CardRepository(context)
            }
        }

        fun get(): CardRepository {
            return INSTANCE ?:
            throw IllegalStateException("CardRepository must be initialized")
        }
    }
}