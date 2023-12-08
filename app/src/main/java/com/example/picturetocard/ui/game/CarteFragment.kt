package com.example.picturetocard.ui.game

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CarteFragment(
    private val card: Card,
    private val gameManager: GameManager? = null,
    private val positionInList: Int? = null
) : Fragment() {
    private var cardAlpha: Float = 1f
    private lateinit var view : View
    private lateinit var fond : ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Gonfler le fichier XML de la carte
        view = inflater.inflate(R.layout.card, container, false)

        // Obtenir les références des ImageView
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val couleurView = view.findViewById<ImageView>(R.id.couleurView)
        val effetView = view.findViewById<ImageView>(R.id.effetView)
        fond = view.findViewById(R.id.fond)

        couleurView.setImageResource(Colors.getIdFromColor(card.color))
        effetView.setImageResource(Effets.getIdFromEffet(card.effet))

        imageView.setImageBitmap(card.imageBitmap)
        fond.setBackgroundColor(ContextCompat.getColor(requireContext(),
            Colors.getStyleFromColor(card.color) ))
        fond.alpha = cardAlpha

        addClick()

        return view
    }

    private fun addClick() {
        if (gameManager != null) {
            fond.setOnClickListener {
                if (gameManager.getCanPlay()) {
                    gameManager.playerPlayCard(positionInList!!)
                }
            }
        }


    }

    fun setAlpha(alpha: Float) {
        cardAlpha = alpha
        if (::fond.isInitialized) {
            fond.alpha = cardAlpha
        }
    }
}