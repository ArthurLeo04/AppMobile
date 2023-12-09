package com.example.picturetocard.ui

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import androidx.fragment.app.FragmentManager
import androidx.room.RoomDatabase
import com.example.picturetocard.database.CardDatabase

class CollectionManager {

    companion object {

        suspend fun fillCollection(database: CardDatabase, context: Context, fragmentManager: FragmentManager, gridLayout: GridLayout) {

            val cards = database.dao().getAllEntities()

            //rempli le gridLayout
            for (i in cards.indices) {
                // Crée un containerId
                val containerId = View.generateViewId()
                // Crée un frame layout
                val frameLayout = FrameLayout(context)
                frameLayout.id = containerId
                val params = GridLayout.LayoutParams()
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                frameLayout.layoutParams = params
                // Ajoute un cardFragment
                CardFragmentManager.setCardFrame(supportFragmentManager = fragmentManager,
                    containerId, cards[i].toCard())

                gridLayout.addView(frameLayout)
            }
        }

    }
}