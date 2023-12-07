package com.example.picturetocard

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.res.Configuration
import com.example.picturetocard.ui.game.CarteFragment
import android.graphics.Typeface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.picturetocard.PictureToCard
import com.example.picturetocard.R
import com.example.picturetocard.game.Card
import com.example.picturetocard.game.GameManager
import com.example.picturetocard.ui.game.TableTypeDialog

class GameActivity : AppCompatActivity() {
    private lateinit var powerUpButton: Button
    private lateinit var scoreView: TextView
    private lateinit var scoreTextAnim : TextView
    private val tableRes = Array<TextView?>(6) { null }
    private val tableCardPlayer = Array<CarteFragment?>(6) { null }
    private val tableCardOppo = Array<CarteFragment?>(6) { null }
    private val tableUp = Array<TextView?>(4) {null}
    private lateinit var helpButton: ImageButton
    private lateinit var pileDisplay: CarteFragment
    private lateinit var viewPlayerPlaying: View
    private lateinit var viewOpponentPlaying: View
    lateinit var gameManager: GameManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Récupérer le gameManager
        val app = application as PictureToCard
        gameManager = GameManager(app.ruleManager)
        gameManager.setGameActivity(this)


        ///// GESTION DE LA MAIN DU JOUEUR //////

        for (i in 1..6) {
            // Ajout des cartes du joueur
            val resourceId = resources.getIdentifier("carte$i", "id", packageName)
            tableCardPlayer[i - 1] = setCardFrame(gameManager.handPlayer.cards[i-1], resourceId,
                needClick = true
            )
        }

        for (i in 1..6) {
            // Ajout du résultat si on joue la carte
            val resourceId = resources.getIdentifier("res$i", "id", packageName)
            val res : TextView = findViewById(resourceId)
            tableRes[i - 1] = res
            val formattedResult = getFormatedRes(gameManager.handPlayer.cards[i-1])
            res.text = formattedResult
            res.textSize = 18f
            res.setTypeface(null, Typeface.BOLD)
        }

        viewPlayerPlaying = findViewById(R.id.viewPlayerPlaying)

        ///// Gestion du score /////
        scoreView = findViewById(R.id.scoreView)
        scoreView.textSize = 40f
        scoreView.setTypeface(null, Typeface.BOLD)

        scoreTextAnim = findViewById(R.id.textScoreAnim)
        scoreTextAnim.textSize = scoreView.textSize * 0.8f
        scoreTextAnim.setTypeface(null, Typeface.BOLD)
        scoreTextAnim.alpha = 0f

        ///// Gestion de la main de l'adv /////

        for (i in 1..6) {
            // Ajout des cartes du joueur
            val resourceId = resources.getIdentifier("carteOpo$i", "id", packageName)
            tableCardOppo[i - 1] = setCardFrame(gameManager.handOppo.cards[i-1], resourceId,
                needClick = true,
                isVisible = false
            )
        }

        viewOpponentPlaying = findViewById(R.id.viewOpoPlaying)

        //// Gestion des vues de jeu

        val currentMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentMode == Configuration.UI_MODE_NIGHT_YES) {
            viewOpponentPlaying.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey))
            viewPlayerPlaying.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey))
        } else {
            viewOpponentPlaying.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
            viewPlayerPlaying.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
        }

        ///// Gestion du bouton de la table des types /////

        helpButton = findViewById(R.id.helpButton)
        helpButton.setOnClickListener {
            showTableType()
        }

        ///// Gestion du bouton powerUp //////

        powerUpButton = findViewById(R.id.button)
        powerUpButton.setOnClickListener {
            // On peut mettre l'affichage de la base de donnée ici :
            gameManager.usePowerUp();
        }

        // Récupére les textsView Up!

        for (i in 1..4) {
            // Ajout des cartes du joueur
            val resourceId = resources.getIdentifier("textUp$i", "id", packageName)
            tableUp[i - 1] = findViewById(resourceId)
            tableUp[i-1]?.textSize = 25f
            tableUp[i-1]?.setTypeface(null, Typeface.BOLD)
            tableUp[i-1]?.alpha = 0f // Rendre invisibles les Up au début
        }

        ///// Commence le jeu

        if (!gameManager.getCanPlay()) {
            gameManager.opponentChoosePlay() // Il faut que l'adversaire choisisse une carte de base
        }

        refreshWhosPlayingView()
    }

    private fun getFormatedRes(indice : Int) : String {
        // Retourne le résultat si on joue la carte à l'indice dans la main du joueur
        val card : Card = gameManager.cards.getCard(indice)
        val result = gameManager.getResult(card)
        return getStringWithPlus(result)
    }

    private fun getStringWithPlus(int : Int) : String {
        // Retourne un toString mais avec + si la valeur est positive
        val formattedResult = if (int > 0) {
            "+$int"
        } else {
            int.toString()
        }
        return formattedResult
    }


    private fun setPileDisplay() {
        // Fixe la carte au dessus de la pile
        if (gameManager.lastPlay != null) {

            pileDisplay = setCardFrame(gameManager.lastPlay!!, R.id.pileDisplay, false)

        }
        else {
            val frameLayout = findViewById<FrameLayout>(R.id.pileDisplay)
            frameLayout.removeAllViews()
        }
    }


    private fun getCarteFragment(positionCard : Int, needClick: Boolean) : CarteFragment {
        // retourne un nouveau fragment de carte
        return CarteFragment.newInstance(positionCard, needClick,null)
    }


    private fun setCardFrame(positionCard: Int, frameId: Int, needClick : Boolean, isVisible : Boolean = true) : CarteFragment {
        // Créez une vue personnalisée pour représenter votre carte
        val carteFragment = getCarteFragment(positionCard, needClick)// Remplacez createCardView par votre logique de création de vue de carte

        if (!isVisible) {
            carteFragment.setAlpha(0f)
        }

        // Obtenez le FragmentManager de votre CardFragment
        val fragmentManager = supportFragmentManager

        // Commencez une transaction de fragment
        val transaction = fragmentManager.beginTransaction()

        // Ajoutez la vue de carte au FrameLayout
        transaction.add(frameId, carteFragment)

        // Validez la transaction
        transaction.commit()

        return carteFragment
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_menu -> {
                // Ouvrir la pop-up ici
                onBackPressed() // équivalent
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (gameManager.isEndGame()) {
            super.onBackPressed()
            return
        }

        // Action effectué lors de l'appuie du bouton retour (ou de Abondonner dans le popUpMenu)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirmation")
        alertDialogBuilder.setMessage("Êtes-vous sûr de vouloir abondonner la partie ?")
        alertDialogBuilder.setPositiveButton("Oui") { dialog, which ->
            // L'utilisateur a confirmé, fermer l'activité
            super.onBackPressed()
        }
        alertDialogBuilder.setNegativeButton("Non") { dialog, which ->
            // L'utilisateur a annulé, ne rien faire
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun refreshAll() {
        refreshPile()
        refreshPlayerHand()
        refreshScore()
        refreshPlayerOppo()
        refreshWhosPlayingView()
        refreshPowerUp()
    }

    fun refreshPlayerHand() {
        var pos = 0
        for (i in 0..<gameManager.handPlayer.cards.size) {
            // Met à jour l'apparence des cartes du joueur
            if (gameManager.handPlayer.isUse[i]) {
                tableCardPlayer[pos]?.setAlpha(0.5f)
                tableRes[pos]?.visibility = View.INVISIBLE
            }
            else if(!gameManager.getCanPlay()) {
                tableRes[pos]?.visibility = View.INVISIBLE
            }
            else {
                tableCardPlayer[pos]?.setAlpha(1f)
                tableRes[pos]?.visibility = View.VISIBLE
                tableRes[pos]?.text = getFormatedRes(gameManager.handPlayer.cards[pos])
            }
            pos ++
        }
    }

    fun refreshPlayerOppo() {
        var pos = 0
        for (i in 0..<gameManager.handOppo.cards.size) {
            // Met à jour l'apparence des cartes du joueur
            if (!gameManager.handOppo.isVisible[i]) {
                tableCardOppo[pos]?.setAlpha(0f)
            }
            else {
                if (gameManager.handOppo.isUse[i]) {
                    tableCardOppo[pos]?.setAlpha(0.5f)
                }
                else {
                    tableCardOppo[pos]?.setAlpha(1f)
                }
            }
            pos ++
        }
    }

    fun refreshPile() {
        setPileDisplay()
    }

    fun refreshScore() {
        scoreView.text = getStringWithPlus(gameManager.getScore())
    }

    fun refreshWhosPlayingView() {
        val animatorAppear = AnimatorInflater.loadAnimator(this, R.animator.appear) as AnimatorSet
        val animatorDisappear = AnimatorInflater.loadAnimator(this, R.animator.disapear) as AnimatorSet

        if (gameManager.getCanPlay()) {
            animatorDisappear.setTarget(viewOpponentPlaying)
            animatorAppear.setTarget(viewPlayerPlaying)
        }
        else {
            animatorDisappear.setTarget(viewPlayerPlaying)
            animatorAppear.setTarget(viewOpponentPlaying)
        }

        animatorAppear.start()
        animatorDisappear.start()
    }

    fun refreshPowerUp() {
        powerUpButton.isEnabled = !gameManager.playerUsedPowerUp
    }

    fun animPlay(playerPlay : Boolean) {
        // Ajoute une animation lorsqu'un joueur joue une carte
        val animator = if (playerPlay) {
            AnimatorInflater.loadAnimator(this, R.animator.player_play) as AnimatorSet
        }
        else {
            AnimatorInflater.loadAnimator(this, R.animator.opponent_play) as AnimatorSet
        }

        val view = findViewById<View>(R.id.viewForAnim)

        view.bringToFront()

        animator.setTarget(view)

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                refreshPile()
                view.alpha = 0f
            }

            override fun onAnimationCancel(p0: Animator) {
                refreshPile()
                view.alpha = 0f
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        animator.start()
    }

    fun animUp() {
        val animatorSet = AnimatorSet()
        val animators = mutableListOf<Animator>()

        for (text : TextView? in tableUp) {
            val animator = AnimatorInflater.loadAnimator(this, R.animator.clignote) as Animator
            animator.setTarget(text)

            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    text?.alpha = 0f
                }

                override fun onAnimationCancel(p0: Animator) {
                    text?.alpha = 0f
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })

            animators.add(animator)
        }

        animatorSet.playTogether(animators as Collection<Animator>?)
        animatorSet.start()

    }

    fun animScore(difference : Int) {
        // Initialisation du texte
        scoreTextAnim.text = getStringWithPlus(difference)

        // Initialisation de l'animation
        val animator = AnimatorInflater.loadAnimator(this, R.animator.defile_score) as AnimatorSet
        animator.setTarget(scoreTextAnim)

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                scoreTextAnim.alpha = 0f
            }

            override fun onAnimationCancel(p0: Animator) {
                scoreTextAnim.alpha = 0f
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        animator.start()
    }

    fun showGameFinishedDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.end_game_pop_up)
        dialog.setCancelable(false) // Empêche le joueur de quitter le menu sans cliquer sur "OK"

        val messageTextView = dialog.findViewById<TextView>(R.id.tvMessage)
        val boText = dialog.findViewById<TextView>(R.id.progressionMatchText)
        val okButton = dialog.findViewById<Button>(R.id.btnOK)

        messageTextView.text = this.getString(
        if (gameManager.getScore() > 0) R.string.winMessage
        else if (gameManager.getScore() == 0) R.string.tieMessage
        else R.string.loseMessage)

        boText.text = gameManager.matchWins.toString() + " - " + gameManager.matchLose

        okButton.setOnClickListener {
            dialog.dismiss() // Ferme le dialogue lorsque le bouton "OK" est cliqué
            if (gameManager.numeroMatch == gameManager.ruleManager.BO ) onBackPressed() // Retour en arriere
            else gameManager.resetGame()
        }

        dialog.show()
    }


    fun showTableType() {
        val dialog = TableTypeDialog(this)
        dialog.show()
    }
}