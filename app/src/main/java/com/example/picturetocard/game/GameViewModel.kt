package com.example.picturetocard.game

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import com.example.picturetocard.GameActivity
import java.util.Random

class GameManager(
    val matrice : MatriceType,
    val cards : CardList
) {
    // Mains des joueurs
    var handPlayer : Hand = cards.randomHand()
    var handOppo : Hand = cards.randomHand()

    // POWER UPS
    var playerUsedPowerUp = false
    var oppoUsedPowerUp = false

    var activePowerUp = false

    // L'activité relié au jeu
    lateinit var gameActivity : GameActivity

    // Est ce le tour du joueur ?
    private var canPlay = true

    // La carte au centre du jeu
    var lastPlay : Card? = null

    // Le score
    var score : Int = 0

    init {
        // Choisi qui est le premier joueur
        val random = Random()
        canPlay = random.nextBoolean()
    }

    fun getResult(cardPlayed : Card) : Int {
        return if (lastPlay != null) {
            if (activePowerUp) matrice.getResult(cardPlayed.color, lastPlay!!.color, cardPlayed.effet)
            else matrice.getResult(cardPlayed.color, lastPlay!!.color)
        } else 1
    }

    fun playerPlayCard(id : Int) {
        setCanPlay(false)
        gameActivity.refreshAll()

        // Player clicked on a card
        val cardIndex = handPlayer.play(id)
        if (cardIndex != 0) { // La carte peut être joué !
            val cardPlayed = cards.getCard(cardIndex)!!

            // Incrémentation du score
            score += getResult(cardPlayed)

            // Changement de la carte à l'affiche
            lastPlay = cardPlayed

            // Mise à jour de l'affichage
            gameActivity.refreshPlayerHand()
        }

        // On retire le pouvoir
        activePowerUp = false

        gameActivity.animPlay(true)
        gameActivity.lifecycleScope.launch {
            delay(1000)

            // Mise à jour de l'affichage
            gameActivity.refreshAll()
            // On fait jouer l'adversaire
            opponentChoosePlay()
        }
    }

    private fun opponentPlayCard(id :Int) {
        // Player clicked on a card
        val cardIndex = handOppo.play(id)
        if (cardIndex != 0) { // La carte peut être joué !
            if (activePowerUp) {
                oppoUsedPowerUp = true
                activatePowerUp()
            }

            val cardPlayed = cards.getCard(cardIndex)!!

            // Décrémente le score
            score -= getResult(cardPlayed)



            // Changement de la carte à l'affiche
            lastPlay = cardPlayed

        }

        // On retire le pouvoir
        activePowerUp = false

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
        var maxScoreWithUp = Int.MIN_VALUE
        var bestCardId = -1
        var bestCardIdWithUp = -1

        for (cardPos in 0..<handOppo.cards.size) {
            val card = cards.getCard(handOppo.cards[cardPos])
            if (card != null && !handOppo.isUse[cardPos]) {
                val scoreWithoutUp = if (lastPlay != null) {
                    matrice.getResult(card.color, lastPlay!!.color)
                }
                else {
                    1
                }
                val scoreWithUp = if (lastPlay != null) {
                    matrice.getResult(card.color, lastPlay!!.color, card.effet)
                }
                else {
                    1
                }

                if (scoreWithoutUp > maxScore) {
                    maxScore = scoreWithoutUp
                    bestCardId = handOppo.cards[cardPos]
                }

                if (scoreWithUp > maxScoreWithUp) {
                    maxScoreWithUp = scoreWithUp
                    bestCardIdWithUp = handOppo.cards[cardPos]
                }

            }
        }

        val powerUp = (maxScore < maxScoreWithUp -1) && !oppoUsedPowerUp
        if (powerUp) {
            bestCardId = bestCardIdWithUp
        }

        gameActivity.lifecycleScope.launch {
            delay(2000)
            opponentPlayCard(bestCardId)
        }

    }

    fun activatePowerUp() {
        activePowerUp = true
        // Lance l'animation dans le jeu
        gameActivity.animUp()
    }

    fun setCanPlay(bool : Boolean) {
        canPlay = bool
        gameActivity.refreshWhosPlayingView()
    }

    fun getCanPlay() : Boolean {return canPlay}
    fun usePowerUp() {
        if (canPlay && !playerUsedPowerUp) {
            playerUsedPowerUp = true
            activatePowerUp()

            gameActivity.refreshPlayerHand() // Met à jour les résultats dans la main du joueur
            gameActivity.refreshPowerUp()
        }
    }

}
