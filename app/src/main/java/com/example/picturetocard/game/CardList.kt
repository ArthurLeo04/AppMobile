package com.example.picturetocard.game

import android.util.Log

class CardList {
    private val cards: MutableList<Card> = mutableListOf()

    fun addCard(card: Card) {
        cards.add(card)
    }

    fun getCard(id: Int): Card? {
        return cards.find { it.id == id }
    }

    fun printCardIds() {
        val tag = "Print"
        val stringBuilder = StringBuilder("Card IDs: ")
        cards.forEach { card ->
            stringBuilder.append("${card.id}, ")
        }
        stringBuilder.deleteCharAt(stringBuilder.length - 2) // Supprime la virgule finale
        Log.d(tag, stringBuilder.toString())
    }
}