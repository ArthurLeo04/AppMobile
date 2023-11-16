package com.example.picturetocard

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import com.example.picturetocard.ui.game.CarteFragment
import android.graphics.Typeface
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.picturetocard.PictureToCard
import com.example.picturetocard.R
import com.example.picturetocard.game.Card
import com.example.picturetocard.game.GameManager

class GameActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var scoreView: TextView
    private val tableRes = Array<TextView?>(6) { null }
    private val tableCardPlayer = Array<CarteFragment?>(6) { null }
    private val tableCardOppo = Array<CarteFragment?>(6) { null }
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
        gameManager = GameManager(app.ruleManager.matrice)
        gameManager.gameActivity = this


        ///// GESTION DE LA MAIN DU JOUEUR //////

        for (i in 1..6) {
            // Ajout des cartes du joueur
            val resourceId = resources.getIdentifier("carte$i", "id", packageName)
            tableCardPlayer[i - 1] = setCardFrame(gameManager.handPlayer.cards[i-1], resourceId, true)
        }

        for (i in 1..6) {
            // Ajout du résultat si on joue la carte
            val resourceId = resources.getIdentifier("res$i", "id", packageName)
            val res : TextView = findViewById(resourceId)
            tableRes[i - 1] = res
            val formattedResult = getFormatedRes(i-1)
            res.text = formattedResult
            res.textSize = 18f
            res.setTypeface(null, Typeface.BOLD)
        }

        viewPlayerPlaying = findViewById(R.id.viewPlayerPlaying)

        ///// Gestion du score /////
        scoreView = findViewById(R.id.scoreView)
        scoreView.textSize = 40f
        scoreView.setTypeface(null, Typeface.BOLD)

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

        // Commence le jeu

        if (!gameManager.getCanPlay()) {
            gameManager.opponentChoosePlay() // Il faut que l'adversaire choisisse une carte de base
        }

        refreshWhosPlayingView()
    }

    private fun getFormatedRes(indice : Int) : String {
        val card : Card = gameManager.cards.getCard(gameManager.handPlayer.cards[indice])!!
        val result = if (gameManager.lastPlay != null) {
            gameManager.matrice.getResult(card.color, gameManager.lastPlay!!.color)
        }
        else {
            +1
        }
        return getStringWithPlus(result)
    }

    private fun getStringWithPlus(int : Int) : String {
        val formattedResult = if (int > 0) {
            "+$int"
        } else {
            int.toString()
        }
        return formattedResult
    }


    private fun setPileDisplay() {
        if (gameManager.lastPlay != null) {

            pileDisplay = setCardFrame(gameManager.lastPlay!!.id, R.id.pileDisplay, false)

        }
    }


    private fun getCarteFragment(cardId: Int, needClick: Boolean) : CarteFragment {
        return CarteFragment.newInstance(cardId, needClick)
    }


    private fun setCardFrame(cardId : Int, frameId: Int, needClick : Boolean, isVisible : Boolean = true) : CarteFragment {
        // Créez une vue personnalisée pour représenter votre carte
        val carteFragment = getCarteFragment(cardId, needClick)// Remplacez createCardView par votre logique de création de vue de carte

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
                showPopupMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPopupMenu() {
        val contextWrapper = ContextThemeWrapper(this, R.style.PopupMenuStyle)
        val popupMenu = PopupMenu(contextWrapper, findViewById(R.id.action_menu))

        popupMenu.menuInflater.inflate(R.menu.game_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            // Gérer les clics des éléments du menu contextuel ici
            when (menuItem.itemId) {
                R.id.action_menu -> {
                    // Action 1
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    fun refreshAll() {
        refreshPile()
        refreshPlayerHand()
        refreshScore()
        refreshPlayerOppo()
    }

    fun refreshPlayerHand() {
        var pos = 0
        for (i in 0..<gameManager.handPlayer.cards.size) {
            // Met à jour l'apparence des cartes du joueur
            if (gameManager.handPlayer.isUse[i]) {
                tableCardPlayer[pos]?.setAlpha(0.5f)
                tableRes[pos]?.visibility = View.INVISIBLE
            }
            else {
                tableRes[pos]?.text = getFormatedRes(pos)
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
            }
            pos ++
        }
    }

    fun refreshPile() {
        setPileDisplay()
    }

    fun refreshScore() {
        scoreView.text = getStringWithPlus(gameManager.score)
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

    fun animPlay(playerPlay : Boolean) {
        val animator = if (playerPlay) {
            AnimatorInflater.loadAnimator(this, R.animator.player_play) as AnimatorSet
        }
        else {
            AnimatorInflater.loadAnimator(this, R.animator.opponent_play) as AnimatorSet
        }

        val view = findViewById<View>(R.id.viewForAnim)

        //view.setBackgroundColor(ContextCompat.getColor(this, R.color.black))

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
}