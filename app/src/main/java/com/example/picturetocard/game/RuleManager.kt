package com.example.picturetocard.game

import com.example.picturetocard.database.CardDatabase
import kotlinx.coroutines.GlobalScope

class RuleManager(database: CardDatabase) {
    // La matrice des régles du jeu
    var matrice = MatriceType.createDefault()

    val BO = 3

}