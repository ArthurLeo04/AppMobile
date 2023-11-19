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

    fun randomHand() : Hand {
        val handSize = 6
        val handCards = mutableListOf<Int>()
        val randomHand = Hand(Array(handSize){0})

        if (cards.size < handSize) {
            Log.e("RandomHand", "Not enough cards available")
            return randomHand
        }

        while (handCards.size < handSize) {
            val randomIndex = (0 until cards.size).random()
            val randomCard = cards[randomIndex]

            if (!handCards.contains(randomCard.id)) {
                handCards.add(randomCard.id)
                randomHand.cards[handCards.size - 1] = randomCard.id
            }
        }

        return randomHand
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