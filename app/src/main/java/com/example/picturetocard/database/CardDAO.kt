package com.example.picturetocard.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface CardDAO {

    @Insert
    fun insert(entity: CardEntity) // Attention ! Dois être lancé depuis une coroutine

    @Query("SELECT *  FROM cards")
    fun getAllEntities(): Flow<List<CardEntity>> // Attention ! Dois être lancé depuis une coroutine

    @Query("SELECT * FROM cards WHERE id = :entityId")
    fun getEntityById(entityId: Int): Flow<CardEntity?> // Attention ! Dois être lancé depuis une coroutine
}