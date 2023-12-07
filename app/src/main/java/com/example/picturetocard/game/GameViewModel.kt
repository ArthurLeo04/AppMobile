package com.example.picturetocard.game

import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import com.example.picturetocard.GameActivity
import java.util.Random

class GameManager(
    val ruleManager: RuleManager
) {
    val cards: CardList = CardList() // Stocke les cartes de la partie seulement

    // Mains des joueurs
    lateinit var handPlayer : Hand
    lateinit var handOppo : Hand

    // POWER UPS
    var playerUsedPowerUp = false
    var oppoUsedPowerUp = false

    var activePowerUp = false

    // L'activité relié au jeu
    private lateinit var gameActivity : GameActivity

    // Est ce le tour du joueur ?
    private var canPlay = true

    // La carte au centre du jeu
    var lastPlay : Int? = null

    // Le score
    private var score : Int = 0
    val scoreToWin = 3
    var numeroMatch = 0
    var matchWins = 0
    var matchLose = 0

    var firstPlayer : Boolean = false

    init {
        // Choisi qui est le premier joueur
        val random = Random()
        canPlay = random.nextBoolean()
        firstPlayer = canPlay

        // Rempli la main des joueurs

    }

    fun setGameActivity(gameActivity: GameActivity) {
        this.gameActivity = gameActivity
        gameActivity.lifecycleScope.launch {
            handPlayer = cards.randomHand()
            handOppo = cards.randomHand()
        }
    }

    fun getResult(cardPlayed : Card) : Int {
        return if (lastPlay != null) {
            if (activePowerUp) ruleManager.matrice.getResult(cardPlayed.color, cards.getCard(lastPlay!!).color, cardPlayed.effet)
            else ruleManager.matrice.getResult(cardPlayed.color, cards.getCard(lastPlay!!).color)
        } else 1
    }

    fun playerPlayCard(id : Int) {
        if (isEndGame()) return // On ne peut pas jouer après la fin de la partie

        setCanPlay(false)
        gameActivity.refreshAll()

        // Player clicked on a card

        if (handPlayer.play(id)) { // La carte peut être joué !
            val cardPlayed = handPlayer.cards[id]

            // Incrémentation du score
            setScore(score + getResult(cards.getCard(cardPlayed)))

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

    fun isEndGame() : Boolean {
        return (handPlayer.isEmpty() && handOppo.isEmpty()) || score >= scoreToWin || score <= -scoreToWin
    }

    private fun opponentPlayCard(id :Int) {
        // Player clicked on a card
        if (handOppo.play(id)) { // La carte peut être joué !
            if (activePowerUp) {
                oppoUsedPowerUp = true
                activatePowerUp()
            }

            val cardPlayed = handOppo.cards[id]

            // Décrémente le score
            setScore(score - getResult(cards.getCard(cardPlayed)))



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
        if (isEndGame()) return // On ne peut pas jouer après la fin de la partie

        var maxScore = Int.MIN_VALUE
        var maxScoreWithUp = Int.MIN_VALUE
        var bestCardId = -1
        var bestCardIdWithUp = -1
        var tailleMain = 0

        for (cardPos in 0..<handOppo.cards.size) {
            val card = cards.getCard(handOppo.cards[cardPos])
            if (!handOppo.isUse[cardPos]) {
                tailleMain++
                val scoreWithoutUp = if (lastPlay != null) {
                    ruleManager.matrice.getResult(card.color, cards.getCard(lastPlay!!).color)
                }
                else {
                    1
                }
                val scoreWithUp = if (lastPlay != null) {
                    ruleManager.matrice.getResult(card.color, cards.getCard(lastPlay!!).color, card.effet)
                }
                else {
                    1
                }

                if (scoreWithoutUp > maxScore) {
                    maxScore = scoreWithoutUp
                    bestCardId = cardPos
                }

                if (scoreWithUp > maxScoreWithUp) {
                    maxScoreWithUp = scoreWithUp
                    bestCardIdWithUp = cardPos
                }

            }
        }

        // L'IA choisi si elle dois utiliser un power up
        val powerUp = (
                maxScore < maxScoreWithUp -1 // Jouer le power Up fait une grosse diff
                        || (maxScore < maxScoreWithUp && tailleMain == 1) // Il ne reste plus qu'une carte en main
                        || score - maxScoreWithUp <= -scoreToWin // Utiliser le boost fait gagner
                        || score + maxScore >= scoreToWin // Utiliser le boost pour ne pas perdre
                )
                && !oppoUsedPowerUp // L'IA ne doit pas avoir déjà joué son powerUp

        if (powerUp) {
            bestCardId = bestCardIdWithUp
        }

        gameActivity.lifecycleScope.launch {
            if (powerUp) activePowerUp = true
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

    fun getScore() : Int {return score}
    fun setScore(n_score : Int) {
        val diff = - score + n_score
        score = n_score
        gameActivity.refreshScore()
        gameActivity.animScore(diff)

        if (isEndGame()) { // On affiche l'ecran de fin au bout d'un certain temps
            gameActivity.lifecycleScope.launch {
                delay(2000)
                gameActivity.showGameFinishedDialog()
            }

            // Mettre à jour les données de match
            if (score > 0) matchWins++
            else matchLose ++
            numeroMatch++
        }

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

    fun resetGame() {
        Log.d("Print","Reset de la game")
        // Remplit la main de chaque joueur
        handPlayer.UnUseCards()
        handOppo.UnUseCards()

        // Remettre le score à 0
        score = 0

        // Redonner le power up au joueurs
        playerUsedPowerUp = false
        oppoUsedPowerUp = false

        // Change qui est le premier joueur
        firstPlayer = !firstPlayer
        canPlay = firstPlayer

        // Remise à 0 de la carte du dessus de la pile
        lastPlay = null

        // Refresh du visuel
        gameActivity.refreshAll()

        if (!canPlay) {opponentChoosePlay()}
    }
}
