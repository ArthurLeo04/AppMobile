package com.example.picturetocard.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface CardDAO { // Attention ! Ces fonctions doivent être lancés depuis une coroutine

    @Insert
    fun insertCard(entity: CardEntity)

    @Insert
    fun insertDeck(entity: DeckEntity)

    @Insert
    fun insertCardInDeck(entity: CardInDeckEntity)

    @Query("SELECT *  FROM cards")
    fun getAllEntities(): List<CardEntity>

    @Query("SELECT id  FROM cards LIMIT :limit")
    fun getCardsIdsWithLimit(limit : Int): List<Int>

    @Query("SELECT id FROM decks LIMIT 1")
    fun getIdFromFirstDeck() : Int

    @Query("SELECT cards.id, cards.Couleur, cards.Effet, cards.PathImage " +
            "FROM cardsInDeck INNER JOIN cards ON cardsInDeck.cardId == cards.id WHERE cardsInDeck.deckId = :deckId")
    fun getCardsInDeck(deckId : Int) : List<CardEntity>

    @Query("SELECT * FROM cardsInDeck")
    fun getAllCardsInDeck() : List<CardInDeckEntity>

    @Query("SELECT * FROM cards WHERE id = :entityId")
    fun getEntityById(entityId: Int): CardEntity?

    @Query("SELECT * FROM cards WHERE id IN (:ids)")
    fun getEntitiesByIds(ids: Array<Int>): List<CardEntity>

    @Query("DELETE FROM cardsInDeck WHERE cardId == :cardId AND deckId == :deckId")
    fun deleteCardInDeck(deckId: Int, cardId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM cardsInDeck WHERE cardId == :cardId AND deckId == :deckId) AS element_present;")
    fun isInDeck(deckId: Int, cardId: Int) : Boolean

    @Query("DELETE FROM cards")
    fun deleteAllCards()

    @Query("DELETE FROM cardsInDeck")
    fun deleteAllCardsInDecks()

    @Query("DELETE FROM decks")
    fun deleteAllDecks()
}