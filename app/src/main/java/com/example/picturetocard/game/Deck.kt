package com.example.picturetocard.game

import com.example.picturetocard.database.CardDatabase
import com.example.picturetocard.database.CardEntity
import com.example.picturetocard.database.CardInDeckEntity
import com.example.picturetocard.database.DeckEntity

class Deck(
    val cartes : Array<Int> // Contient les id dans la BD des cartes du deck
) {

    companion object {
        suspend fun insertTestDeck(database: CardDatabase) {
            // Ajoute un deck à la base de donnée

            // Vide la BD
            database.dao().deleteAllCards()
            database.dao().deleteAllDecks()
            database.dao().deleteAllCardsInDecks()

            //Crée 6 cartes
            database.dao().insertCard(CardEntity(0,1,""))
            database.dao().insertCard(CardEntity(1,2,""))
            database.dao().insertCard(CardEntity(2,3,""))
            database.dao().insertCard(CardEntity(3,4,""))
            database.dao().insertCard(CardEntity(4,5,""))
            database.dao().insertCard(CardEntity(5,6,""))

            // Crée une instance de deck
            database.dao().insertDeck(DeckEntity("Test"))

            // Récupére 6 cartes au pif
            val cardsDeck : List<Int> = database.dao().getCardsIdsWithLimit(6)
            val indexDeck = database.dao().getIdFromFirstDeck()

            for (id in cardsDeck) {
                database.dao().insertCardInDeck(CardInDeckEntity(indexDeck, id))
            }
        }
    }
}