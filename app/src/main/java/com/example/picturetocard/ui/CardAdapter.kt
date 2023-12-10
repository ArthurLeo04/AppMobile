package com.example.picturetocard.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.picturetocard.R
import com.example.picturetocard.database.CardDatabase
import com.example.picturetocard.database.CardEntity
import com.example.picturetocard.database.CardInDeckEntity
import com.example.picturetocard.ui.game.CarteFragment
import com.example.picturetocard.ui.modification.ModificationFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CardAdapter(
    private val cardList : List<CardEntity>,
    private val database: CardDatabase,
    private val deckId: Int,
    private val modificationFragment: ModificationFragment? = null
) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    private val listHolders = mutableListOf<ViewHolder>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        suspend fun setAlpha(database : CardDatabase, deckId : Int, cardEntity: CardEntity) {
            if (database.dao().isInDeck(deckId, cardEntity.id)) {
                itemView.alpha = 0.4f
            }
            else {
                itemView.alpha = 1f
            }
        }

        fun addClick(
            database: CardDatabase,
            cardEntity: CardEntity,
            deckId: Int,
            modificationFragment: ModificationFragment
        ) {
            itemView.setOnClickListener {
                GlobalScope.launch {
                    if (!database.dao()
                            .isInDeck(deckId, cardEntity.id)
                    ) { // La carte n'est pas déjà dans le deck
                        if (database.dao()
                                .getCardsInDeck(deckId).size < 6
                        ) { // Le deck n'est pas plein
                            database.dao().insertCardInDeck(CardInDeckEntity(deckId, cardEntity.id))
                            itemView.alpha = 0.4f
                            modificationFragment.refreshDeck()
                        }
                    }
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        val view = ViewHolder(itemView)
        listHolders.add(view)
        return view
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Met à jour la vue de la carte avec les infos genre couleur, effet et image
        CarteFragment.setInfosOnView(holder.itemView, cardList[position].toCard(), holder.itemView.context, 1f)

        if (modificationFragment != null) {
            // Met à jour l'alpha
            GlobalScope.launch {
                holder.setAlpha(database, deckId, cardList[position])
            }
            holder.addClick(database, cardList[position], deckId, modificationFragment)
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    suspend fun refresh() {
        for (position in listHolders.indices) {
            listHolders[position].setAlpha(database, deckId, cardList[position])
        }
    }

}