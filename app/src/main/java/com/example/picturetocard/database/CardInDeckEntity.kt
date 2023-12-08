package com.example.picturetocard.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "cardsInDeck", primaryKeys = ["deckId","cardId"],
    foreignKeys = [
        ForeignKey(entity = CardEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("cardId"),
        onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = DeckEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("deckId"),
        onDelete = ForeignKey.CASCADE)
    ])
data class CardInDeckEntity(
    val deckId : Int,
    val cardId : Int
)