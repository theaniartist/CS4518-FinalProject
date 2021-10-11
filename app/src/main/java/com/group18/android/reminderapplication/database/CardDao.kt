package com.group18.android.reminderapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.group18.android.reminderapplication.model.Card
import java.util.*

@Dao
interface CardDao {
    @Query("SELECT * FROM card")
    fun getCards(): LiveData<List<Card>>

    @Query("SELECT * FROM card WHERE id=(:id)")
    fun getCard(id: UUID): LiveData<Card?>

    @Update
    fun updateCard(card: Card)

    @Insert
    fun addCard(card: Card)
}