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
import com.example.picturetocard.PictureToCard
import com.example.picturetocard.R
import com.example.picturetocard.game.Colors
import com.example.picturetocard.game.Effets
import com.example.picturetocard.game.GameManager
import com.example.picturetocard.game.getIdFromColor
import com.example.picturetocard.game.getIdFromEffet
import com.example.picturetocard.game.getStyleFromColor

class CarteFragment : Fragment() {
    private var cardAlpha: Float = 1f
    private lateinit var fond : ConstraintLayout
    private lateinit var gameManager : GameManager
    companion object {
        fun newInstance(cardId: Int, needClickable: Boolean, image: Bitmap?,color1:String,color2:String): CarteFragment {
            val fragment = CarteFragment()
            val args = Bundle()
            args.putInt("cardId", cardId)
            args.putBoolean("canClick", needClickable)
            args.putParcelable("image", image)
            args.putString("color1",color1)
            args.putString("color2",color2)
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

            val card = ruleManager.cards.getCard(cardId)!!
            // Récupére la couleur et l'effet de la carte

            arguments?.getString("color1")?.let { couleurView.setImageResource(getIdFromColor(Colors.valueOf(it))) }
            arguments?.getString("color2")?.let { effetView.setImageResource(getIdFromEffet(Effets.valueOf(it))) }

            imageView.setImageBitmap(image)
            fond.setBackgroundColor(ContextCompat.getColor(requireContext(),
                arguments?.getString("color1")?.let { getStyleFromColor(Colors.valueOf(it)) }!!))
            fond.alpha = cardAlpha

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