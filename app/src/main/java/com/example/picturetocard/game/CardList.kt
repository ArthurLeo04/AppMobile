package com.example.picturetocard.game

import android.util.Log
import com.example.picturetocard.database.CardDatabase
import com.example.picturetocard.database.CardEntity
import com.example.picturetocard.ui.menu.MenuFragment

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

    suspend fun fillCardsFromDB(database: CardDatabase, deckId: Int, gameManager: GameManager, menuFragment: MenuFragment) {
        // Va chercher dans la BD les cartes pour la partie, et remplis les mains des joueurs
        val playerHand = Hand(Array(6){0}) // Rempli la main de cartes null

        // Prend les cartes du deck
        val cardsDeck : List<CardEntity> = database.dao().getCardsInDeck(deckId)

        for (i : Int in cardsDeck.indices) {
            if (i > 5) break // A enlever pour plus tard ...
            addCard(cardsDeck[i].toCard()) // Besoin d'ajouter les images
            playerHand.cards[i] = cards.size -1
            Log.d("Print", ""+(cards.size-1))
            Log.d("Print", ""+playerHand.cards[i])
        }

        // Set les mains des joueurs
        gameManager.handPlayer = playerHand
        gameManager.handOppo = randomHand()

        gameManager.startGame(menuFragment)
    }
}