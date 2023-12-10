package com.example.picturetocard.ui.modification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.picturetocard.PictureToCard
import com.example.picturetocard.R
import com.example.picturetocard.database.CardComparator
import com.example.picturetocard.database.CardDatabase
import com.example.picturetocard.database.CardEntity
import com.example.picturetocard.database.CardInDeckEntity
import com.example.picturetocard.game.Card
import com.example.picturetocard.game.Colors
import com.example.picturetocard.game.Effets
import com.example.picturetocard.ui.CardFragmentManager
import com.example.picturetocard.ui.CollectionManager
import com.example.picturetocard.ui.game.CarteFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Collections

class ModificationFragment : CollectionManager() {

    private val deckFrameIds = intArrayOf(
        R.id.carte1,
        R.id.carte2,
        R.id.carte3,
        R.id.carte4,
        R.id.carte5,
        R.id.carte6
    )

    private lateinit var database : CardDatabase
    private lateinit var view: View
    private val listFragments = mutableListOf<CarteFragment>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(R.layout.fragment_modification, container, false)

        //// Partie collection

        // Récupére les cartes
        database = (requireActivity().application as PictureToCard).database

        val modificationFragment = this
        GlobalScope.launch {
            fillCollection(database, view, R.id.recyclerModification , modifcationFragment = modificationFragment)
            fillDeckView()
        }

        return view
    }

    suspend fun fillDeckView() {

        val deckId = database.dao().getIdFromFirstDeck()

        // Récupére les ids


        //Récupére le deck de 6 cartes
        val cards = database.dao().getCardsInDeck(deckId)

        // Trier la liste des cartes
        Collections.sort(cards, CardComparator())

        for (i in cards.indices) {
            createFragmentForLayout(deckFrameIds[i], deckId, cards[i])
        }

    }

    private fun createFragmentForLayout(idView : Int, deckId : Int, cardEntity: CardEntity) {
        val layout : FrameLayout = view.findViewById(idView)

        val carteFragment = CardFragmentManager.setCardFrame(supportFragmentManager = parentFragmentManager,
            idView, cardEntity.toCard())

        listFragments.add(carteFragment)

        // Add click
        layout.setOnClickListener {
            GlobalScope.launch {
                // On retire la carte du deck,
                database.dao().deleteCardInDeck(deckId, carteFragment.getCard().id)

                refreshDeck()
                refreshCollection() // On met à jour l'affichage de la collection
            }
        }
    }

    suspend fun refreshDeck() {
        // Retire de l'affichage toutes les vues puis les remets
        for (fragment in listFragments) {
            fragment.setAlpha(0f)
        }

        val deckId = database.dao().getIdFromFirstDeck()

        //Récupére le deck de 6 cartes
        val cards = database.dao().getCardsInDeck(deckId)

        // Trier la liste des cartes
        Collections.sort(cards, CardComparator())

        requireActivity().runOnUiThread {
            for (i in cards.indices) {
                if (i < listFragments.size) {
                    listFragments[i].setCard(cards[i].toCard())
                }
                else { // On dois créer la vue
                    createFragmentForLayout(deckFrameIds[i], deckId, cards[i])
                }
            }
        }
    }

}

