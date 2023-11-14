package com.example.picturetocard

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
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var gameManager: GameManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Récupérer le gameManager
        val app = application as PictureToCard
        gameManager = app.gameManager
        gameManager.gameActivity = this

        ///// GESTION DE LA PILE //////
        pileDisplay = setCardFrame(gameManager.lastPlay.id, R.id.pileDisplay, false)

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

        ///// Gestion du score /////
        scoreView = findViewById(R.id.scoreView)
        scoreView.textSize = 40f
        scoreView.setTypeface(null, Typeface.BOLD)

    }

    private fun getFormatedRes(indice : Int) : String {
        val card : Card = gameManager.cards.getCard(gameManager.handPlayer.cards[indice])!!
        val result = gameManager.matrice.getResult(card.color, gameManager.lastPlay.color)
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

    private fun setCardFrame(cardId : Int, frameId: Int, needClick : Boolean) : CarteFragment {
        // Créez une vue personnalisée pour représenter votre carte
        val carteFragment = CarteFragment.newInstance(cardId, needClick) // Remplacez createCardView par votre logique de création de vue de carte

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
        refreshPlayerHand()
        refreshPile()
        refreshScore()
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

    fun refreshPile() {
        pileDisplay = setCardFrame(gameManager.lastPlay.id, R.id.pileDisplay, false)
    }

    fun refreshScore() {
        scoreView.text = getStringWithPlus(gameManager.score)
    }
}