package com.example.picturetocard.ui

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picturetocard.R
import com.example.picturetocard.database.CardComparator
import com.example.picturetocard.database.CardDatabase
import com.example.picturetocard.database.CardEntity
import com.example.picturetocard.ui.game.CarteFragment
import com.example.picturetocard.ui.modification.ModificationFragment
import java.util.Collections


open class CollectionManager : Fragment() {

    lateinit var recyclerView: RecyclerView

    suspend fun fillCollection(database: CardDatabase, view: View, idRecyclerView: Int,
                               modifcationFragment : ModificationFragment? = null) {
        recyclerView = view.findViewById(idRecyclerView)

        val nbeColomn = if (modifcationFragment != null) 4 else 2
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), nbeColomn)

        recyclerView.layoutManager = layoutManager

        recyclerView.addItemDecoration(
            MarginItemDecoration(6)
        )

        val cards = database.dao().getAllEntities()
        val deckId = database.dao().getIdFromFirstDeck()

        // Trier la liste des cartes
        Collections.sort(cards, CardComparator())

        val adapter = CardAdapter(cards, database, deckId, modifcationFragment)
        recyclerView.adapter = adapter
    }


    suspend fun refreshCollection() {
        (recyclerView.adapter as CardAdapter).refresh()
    }

}