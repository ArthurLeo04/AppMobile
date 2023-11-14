package com.example.picturetocard.game

import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import com.example.picturetocard.GameActivity

class GameManager(

) {
    var handPlayer : Hand
    lateinit var handOppo : Hand
    public lateinit var gameActivity : GameActivity
    public var canPlay = true

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

    fun playerPlayCard(id : Int) {
        // Player clicked on a card
        val cardIndex = handPlayer.play(id)
        if (cardIndex != 0) { // La carte peut être joué !
            val cardPlayed = cards.getCard(cardIndex)!!

            // Incrémentation du score
            score += matrice.getResult(cardPlayed.color, lastPlay.color)

            // Changement de la carte à l'affiche
            lastPlay = cardPlayed

            // Mise à jour de l'affichage
            gameActivity.refreshAll()
        }


    }

    fun opponentPlayCard(id :Int) {
        // Player clicked on a card
        val cardIndex = handOppo.play(id)
        if (cardIndex != 0) { // La carte peut être joué !
            val cardPlayed = cards.getCard(cardIndex)!!

            // Décrémenter le score
            score -= matrice.getResult(cardPlayed.color, lastPlay.color)

            // Changement de la carte à l'affiche
            lastPlay = cardPlayed

            // Mise à jour de l'affichage
            gameActivity.refreshAll()

            canPlay = true
        }
    }

    fun opponentChoosePlay() {
        gameActivity.lifecycleScope.launch {
            delay(2000)
            var maxScore = Int.MIN_VALUE
            var bestCardId = -1

            for (cardId in handOppo.cards) {
                val card = cards.getCard(cardId)
                if (card != null) {
                    val score = matrice.getResult(card.color, lastPlay.color)

                    if (score > maxScore) {
                        maxScore = score
                        bestCardId = cardId
                    }
                }
            }

            opponentPlayCard(bestCardId)
        }

    }
}
