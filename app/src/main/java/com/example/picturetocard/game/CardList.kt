package com.example.picturetocard.game

import com.example.picturetocard.database.CardDatabase
import com.example.picturetocard.database.CardEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class CardList(
    private val database: CardDatabase
) {
    // private val cards: MutableList<Card> = mutableListOf()

    fun addCard(card: Card) : Int {
        val myDao = database.dao()
        val entity = CardEntity(color = card.color.ordinal, effet = card.effet.ordinal, imagePath = "image_path")
        //myDao.insert(entity) TODO fix
        return entity.id
        //cards.add(card)
    }

    fun getCard(id: Int): Card? {
        //return cards.find { it.id == id }
        val myDao = database.dao()

        val cardEntity: CardEntity? = myDao.getEntityById(id)

        var card : Card? = null

        if (cardEntity != null) {
            card = Card(Colors.fromInt(cardEntity.color), Effets.fromInt(cardEntity.effet))
            card.id = id // On fixe l'id comme dans la BD
            // Faites ce que vous voulez avec les valeurs de l'entité
        } else {
            // L'élément est nul, gérer le cas approprié si nécessaire
        }

        return card
    }

    fun randomHand(gameManager: GameManager) : Hand {
        val randomHand = Hand(Array(6){Card(Colors.FEU, Effets.FEU)}) // Rempli la main de cartes null

        for (i in 0..5) {
            val card = Card(Colors.getRandom(), Effets.getRandom()) // Crée une carte au pif

            // Ajoute la carte à la base de donnée et à la partie
            addCard(card)
            gameManager.AddCard(card)

            randomHand.cards[i] = card

        }

        return randomHand
    }
}