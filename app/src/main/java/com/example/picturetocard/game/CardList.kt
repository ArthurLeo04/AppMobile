package com.example.picturetocard.game

import android.util.Log
import com.example.picturetocard.database.CardDatabase
import com.example.picturetocard.database.CardEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CardList( // Liste des cartes dans la partie
) {
    private val cards: MutableList<Card> = mutableListOf()

    fun addCard(card: Card) {
        cards.add(card)
    }

    fun getCard(id: Int): Card {
        return cards[id]
    }

    fun randomHand() : Hand {
        val randomHand = Hand(Array(6){0}) // Rempli la main de cartes null

        for (i in 0..5) {
            val card = Card(Colors.getRandom(), Effets.getRandom()) // Crée une carte au pif

            // Ajoute la carte à la base de donnée et à la partie
            addCard(card) // On fixe l'id

            randomHand.cards[i] = cards.size -1 // La position de la nouvelle carte

        }

        return randomHand
    }
}