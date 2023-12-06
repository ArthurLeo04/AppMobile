package com.example.picturetocard.ui.game

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.picturetocard.GameActivity
import com.example.picturetocard.PictureToCard
import com.example.picturetocard.R
import com.example.picturetocard.game.Card
import com.example.picturetocard.game.Colors
import com.example.picturetocard.game.Effets
import com.example.picturetocard.game.GameManager
import kotlinx.coroutines.launch

class CarteFragment : Fragment() {
    private var cardAlpha: Float = 1f
    private lateinit var fond : ConstraintLayout
    private lateinit var gameManager : GameManager
    companion object {
        fun newInstance(indiceCard: Int, needClickable: Boolean, image: Bitmap?): CarteFragment {
            val fragment = CarteFragment()
            val args = Bundle()
            args.putInt("cardId", indiceCard)
            args.putBoolean("canClick", needClickable)
            args.putParcelable("image", image)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Gonfler le fichier XML de la carte
        val view = inflater.inflate(R.layout.card, container, false)

        val cardId = arguments?.getInt("cardId", 0)
        val canClick = arguments?.getBoolean("canClick", false)

        val image = arguments?.getParcelable<Bitmap>("image")


        // Obtenir les références des ImageView
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val couleurView = view.findViewById<ImageView>(R.id.couleurView)
        val effetView = view.findViewById<ImageView>(R.id.effetView)
        fond = view.findViewById<ConstraintLayout>(R.id.fond)

        // Recherche de l'application pour avoir le ruleManager pour avoir les cartes
        val application = requireActivity().application as PictureToCard
        val ruleManager = application.ruleManager

        // On récupére le gameManager si on est dans une GameActivity
        val activity = requireActivity()
        if (activity is GameActivity) {
            // On fixe le gameManager
            gameManager = activity.gameManager
        }

        cardId?.let {

            lifecycleScope.launch {
                val card = gameManager.GetCardById(cardId)!!
                // Récupére la couleur et l'effet de la carte
                couleurView.setImageResource(Colors.getIdFromColor(card.color))
                effetView.setImageResource(Effets.getIdFromEffet(card.effet))
                imageView.setImageBitmap(image)
                fond.setBackgroundColor(ContextCompat.getColor(requireContext(),
                    Colors.getStyleFromColor(card.color)))
                fond.alpha = cardAlpha
            }

            // TODO Ajouter l'image

            if (canClick == true) {
                if (::gameManager.isInitialized) { // On peut appuyer sur la carte
                    fond.setOnClickListener {
                        if (gameManager.getCanPlay()) {
                            gameManager.playerPlayCard(cardId)
                        }

                    }
                }
            }
        }

        return view
    }

    public fun setAlpha(n_alpha: Float) {
        cardAlpha = n_alpha
        if (::fond.isInitialized) {
            fond.alpha = cardAlpha
        }
    }
}