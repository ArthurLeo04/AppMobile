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
import com.example.picturetocard.GameActivity
import com.example.picturetocard.R
import com.example.picturetocard.game.GameManager
import com.example.picturetocard.game.getIdFromColor
import com.example.picturetocard.game.getIdFromEffet
import com.example.picturetocard.game.getStyleFromColor

class CarteFragment : Fragment() {
    private lateinit var gameManager: GameManager
    private var cardAlpha: Float = 1f
    private lateinit var fond : ConstraintLayout
    companion object {
        fun newInstance(cardId: Int, needClickable: Boolean, image: Bitmap?): CarteFragment {
            val fragment = CarteFragment()
            val args = Bundle()
            args.putInt("cardId", cardId)
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

        // Recherche du gameManager
        val gameActivity = activity as? GameActivity
        if (gameActivity != null) {
            gameManager = gameActivity.gameManager
            // Utilisez gameViewModel dans votre fragment

        }

        cardId?.let {

            val card = gameManager.cards.getCard(cardId)!!
            // Récupére la couleur et l'effet de la carte
            couleurView.setImageResource(getIdFromColor(card.color))
            effetView.setImageResource(getIdFromEffet(card.effet))
            imageView.setImageBitmap(image)
            fond.setBackgroundColor(ContextCompat.getColor(requireContext(),
                getStyleFromColor(card.color)))
            fond.alpha = cardAlpha

            // TODO Ajouter l'image

            if (canClick == true) {
                fond.setOnClickListener {
                    if (gameManager.getCanPlay()) {
                        gameManager.playerPlayCard(cardId)
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