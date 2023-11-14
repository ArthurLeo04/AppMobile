package com.example.picturetocard.game

import android.util.Log

class GameManager(

) {
    var handPlayer : Hand
    lateinit var handOppo : Hand

    val cards : CardList = CardList()

    lateinit var lastPlay : Card

    var score : Int = 0

    var matrice = MatriceType.createDefault()

    init {
        val carte1 = Card(Colors.ROCHE, Effets.PLUS_UN)
        cards.addCard(carte1)
        lastPlay = carte1

        val carte2 = Card(Colors.EAU, Effets.FEU)
        cards.addCard(carte2)

        val carte3 = Card(Colors.METAL, Effets.PLUS_UN)
        cards.addCard(carte3)

        val carte4 = Card(Colors.ROCHE, Effets.FEU)
        cards.addCard(carte4)

        val carte5 = Card(Colors.NATURE, Effets.AIR)
        cards.addCard(carte5)

        val carte6 = Card(Colors.GLACE, Effets.FOUDRE)
        cards.addCard(carte6)

        handPlayer = Hand(arrayOf(carte1.id,carte2.id,carte3.id,carte4.id,carte5.id,carte6.id))
        handOppo = Hand(arrayOf(carte1.id,carte2.id,carte3.id,carte4.id,carte5.id,carte6.id))
    }
}
