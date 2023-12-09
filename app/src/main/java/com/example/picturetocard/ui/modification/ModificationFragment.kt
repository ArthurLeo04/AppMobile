package com.example.picturetocard.ui.modification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.picturetocard.PictureToCard
import com.example.picturetocard.R
import com.example.picturetocard.game.Card
import com.example.picturetocard.game.Colors
import com.example.picturetocard.game.Effets
import com.example.picturetocard.ui.CardFragmentManager
import com.example.picturetocard.ui.CollectionManager
import com.example.picturetocard.ui.game.CarteFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ModificationFragment : Fragment() {

    private val handize = 6 // Remplacez cela par la variable dont vous disposez

    private val iconResIds = intArrayOf(
        R.drawable.air,
        R.drawable.feu,
        R.drawable.eau,
        R.drawable.metal,
        R.drawable.nature,
        R.drawable.air
    ) // Remplacez cela par les identifiants de ressources de vos icônes

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modification, container, false)

        //// Partie collection

        // Trouver la GridLayout dans le nouveau layout
        val gridLayout = view.findViewById<GridLayout>(R.id.gridLayout)

        // Récupére les cartes
        val database = (requireActivity().application as PictureToCard).database

        GlobalScope.launch {
            CollectionManager.fillCollection(database, requireContext(), parentFragmentManager, gridLayout)
        }

        /////// Partie Deck

        //Rempli les 6 Frame du deck
        val deckFrameIds = intArrayOf(
            R.id.carte1,
            R.id.carte2,
            R.id.carte3,
            R.id.carte4,
            R.id.carte5,
            R.id.carte6
        )

        for (i in 0 until handize) {
            val frameLayout = view.findViewById<FrameLayout>(deckFrameIds[i])

            // Ajoutez un ImageView à chaque conteneur avec l'icône correspondante
            val imageView = ImageView(requireContext())
            imageView.setImageResource(iconResIds[i])

            // Définissez la taille des ImageView ici (par exemple, 48dp x 48dp)
            val imageSize = resources.getDimensionPixelSize(R.dimen.icon_size)
            imageView.layoutParams = ViewGroup.LayoutParams(imageSize, imageSize)

            frameLayout.addView(imageView)
        }

        return view
    }
}

