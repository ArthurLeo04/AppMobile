package com.example.picturetocard.ui.modification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.picturetocard.R

class ModificationFragment : Fragment() {

    private val numberOfContainers = 6 // Remplacez cela par la variable dont vous disposez

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

        // Trouver la GridLayout dans le nouveau layout
        val gridLayout = view.findViewById<GridLayout>(R.id.gridLayout)
         //rempli le gridLayout
        for (i in 0 until numberOfContainers) {
            val containerId = View.generateViewId()
            val frameLayout = FrameLayout(requireContext())
            frameLayout.id = containerId
            val params = GridLayout.LayoutParams()
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            frameLayout.layoutParams = params

            // Ajoutez un ImageView à chaque conteneur avec l'icône correspondante
            val imageView = ImageView(requireContext())
            imageView.setImageResource(iconResIds[i])

            // Définissez la taille des ImageView ici (par exemple, 48dp x 48dp)
            val imageSize = resources.getDimensionPixelSize(R.dimen.icon_size)
            imageView.layoutParams = ViewGroup.LayoutParams(imageSize, imageSize)

            frameLayout.addView(imageView)

            gridLayout.addView(frameLayout)
        }
        //Rempli les 6 Frame du deck
        val deckFrameIds = intArrayOf(
            R.id.carte1,
            R.id.carte2,
            R.id.carte3,
            R.id.carte4,
            R.id.carte5,
            R.id.carte6
        )

        for (i in 0 until numberOfContainers) {
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

