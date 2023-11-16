package com.example.picturetocard.game

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import com.example.picturetocard.GameActivity
import java.util.Random

class GameManager(
    val matrice : MatriceType
) {
    var handPlayer : Hand
    lateinit var handOppo : Hand
    public lateinit var gameActivity : GameActivity
    private var canPlay = true

    val cards : CardList = CardList()

    var lastPlay : Card? = null

    var score : Int = 0

    init {
        val carte1 = Card(Colors.ROCHE, Effets.PLUS_UN)
        cards.addCard(carte1)

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

        // Choisi qui est le premier joueur
        val random = Random()
        canPlay = random.nextBoolean()
    }

    fun playerPlayCard(id : Int) {
        setCanPlay(false)
        gameActivity.refreshAll()

        // Player clicked on a card
        val cardIndex = handPlayer.play(id)
        if (cardIndex != 0) { // La carte peut être joué !
            val cardPlayed = cards.getCard(cardIndex)!!

            // Incrémentation du score
            score += if (lastPlay != null) {
                matrice.getResult(cardPlayed.color, lastPlay!!.color)
            } else {
                1
            }

            // Changement de la carte à l'affiche
            lastPlay = cardPlayed

            // Mise à jour de l'affichage
            gameActivity.refreshPlayerHand()
        }

        gameActivity.animPlay(true)
        gameActivity.lifecycleScope.launch {
            delay(1000)

            // Mise à jour de l'affichage
            gameActivity.refreshAll()
            // On fait jouer l'adversaire
            opponentChoosePlay()
        }
    }

    fun opponentPlayCard(id :Int) {
        // Player clicked on a card
        val cardIndex = handOppo.play(id)
        if (cardIndex != 0) { // La carte peut être joué !
            val cardPlayed = cards.getCard(cardIndex)!!

            // Décrémenter le score
            score -= if (lastPlay != null) {
                matrice.getResult(cardPlayed.color, lastPlay!!.color)
            } else {
                1
            }

            // Changement de la carte à l'affiche
            lastPlay = cardPlayed


        }



        gameActivity.animPlay(false)
        gameActivity.lifecycleScope.launch {
            delay(1000)

            // On fait jouer l'adversaire
            setCanPlay(true)

            // Mise à jour de l'affichage
            gameActivity.refreshAll()

        }

    }

    fun opponentChoosePlay() {

        var maxScore = Int.MIN_VALUE
        var bestCardId = -1

        for (cardPos in 0..<handOppo.cards.size) {
            val card = cards.getCard(handOppo.cards[cardPos])
            if (card != null && !handOppo.isUse[cardPos]) {
                val score = if (lastPlay != null) {
                    matrice.getResult(card.color, lastPlay!!.color)
                }
                else {
                    1
                }

                if (score > maxScore) {
                    maxScore = score
                    bestCardId = handOppo.cards[cardPos]
                }
            }
        }

        gameActivity.lifecycleScope.launch {
            delay(2000)
            opponentPlayCard(bestCardId)
        }

    }

    fun setCanPlay(bool : Boolean) {
        canPlay = bool
        gameActivity.refreshWhosPlayingView()
    }

    fun getCanPlay() : Boolean {return canPlay}

}
