package com.example.picturetocard

import CarteFragment
import PlayerHandAdapter
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.picturetocard.game.Card
import com.example.picturetocard.game.GameManager

class GameActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var scoreView: TextView
    private val tableRes = Array<TextView?>(6) { null }
    private val tableCardPlayer = Array<FrameLayout?>(6) { null }
    private val tableCardOppo = Array<FrameLayout?>(6) { null }
    private lateinit var helpButton: ImageButton
    private lateinit var pileDisplay: FrameLayout
    private lateinit var gameManager: GameManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Récupérer le gameManager
        val app = application as PictureToCard
        gameManager = app.gameManager

        ///// GESTION DE LA PILE //////
        setCardFrame(gameManager.lastPlay.id, R.id.pileDisplay)

        ///// GESTION DE LA MAIN DU JOUEUR //////

        setCardFrame(1, R.id.carte1)

        for (i in 1..6) {
            // Ajout des cartes du joueur
            val resourceId = resources.getIdentifier("carte$i", "id", packageName)
            tableCardPlayer[i - 1] = findViewById(resourceId)
            setCardFrame(gameManager.handPlayer.cards[i-1], resourceId)
        }

        for (i in 1..6) {
            // Ajout du résultat si on joue la carte
            val resourceId = resources.getIdentifier("res$i", "id", packageName)
            val res : TextView = findViewById(resourceId)
            tableRes[i - 1] = res
            val card : Card = gameManager.cards.getCard(gameManager.handPlayer.cards[i-1])!!
            val result = gameManager.matrice.getResult(card.color, gameManager.lastPlay.color)
            val formattedResult = if (result > 0) {
                "+$result"
            } else {
                result.toString()
            }
            res.text = formattedResult
            res.textSize = 18f
            res.setTypeface(null, Typeface.BOLD)
        }

    }

    private fun setCardFrame(cardId : Int, frameId: Int) {
        // Créez une vue personnalisée pour représenter votre carte
        val carteFragment = CarteFragment.newInstance(cardId) // Remplacez createCardView par votre logique de création de vue de carte

        // Obtenez le FragmentManager de votre CardFragment
        val fragmentManager = supportFragmentManager

        // Commencez une transaction de fragment
        val transaction = fragmentManager.beginTransaction()

        // Ajoutez la vue de carte au FrameLayout
        transaction.add(frameId, carteFragment)

        // Validez la transaction
        transaction.commit()
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
}