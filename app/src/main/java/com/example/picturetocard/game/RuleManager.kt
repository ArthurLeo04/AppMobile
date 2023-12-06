package com.example.picturetocard.game

import com.example.picturetocard.database.CardDatabase

class RuleManager(database: CardDatabase) {
    // La matrice des régles du jeu
    var matrice = MatriceType.createDefault()

    // Liste des cartes dans le jeu, à remplacer plus tard par une BD
    val cards : CardList = CardList(database)

    val BO = 3

}