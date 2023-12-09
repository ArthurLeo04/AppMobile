package com.example.picturetocard.ui

import androidx.fragment.app.FragmentManager
import com.example.picturetocard.game.Card
import com.example.picturetocard.game.GameManager
import com.example.picturetocard.ui.game.CarteFragment


class CardFragmentManager {

    companion object {
        private fun getCarteFragment(card : Card) : CarteFragment {
            // retourne un nouveau fragment de carte
            return CarteFragment(card)
        }

        private fun getCarteFragment(gameManager: GameManager, positionCard: Int) : CarteFragment {
            return CarteFragment(gameManager.cards.getCard(positionCard), gameManager, positionCard)
        }

        private fun setCardFragmentInLayout(supportFragmentManager: FragmentManager,
                         frameId: Int, carteFragment: CarteFragment,
                         isVisible : Boolean = true) : CarteFragment {
            if (!isVisible) {
                carteFragment.setAlpha(0f)
            }

            // Commencez une transaction de fragment
            val transaction = supportFragmentManager.beginTransaction()

            // Ajoutez la vue de carte au FrameLayout
            transaction.add(frameId, carteFragment)

            // Validez la transaction
            transaction.commit()

            return carteFragment
        }

        fun setCardFrame(supportFragmentManager: FragmentManager , frameId: Int,
                                 card: Card, isVisible : Boolean = true) : CarteFragment {
            // Créez une vue personnalisée pour représenter votre carte
            val carteFragment = getCarteFragment(card)
            return setCardFragmentInLayout(supportFragmentManager, frameId, carteFragment, isVisible)
        }

        fun setCardFrame(supportFragmentManager: FragmentManager , frameId: Int,
                         positionCard: Int, gameManager: GameManager, isVisible : Boolean = true) : CarteFragment {
            // Créez une vue personnalisée pour représenter votre carte
            val carteFragment = getCarteFragment(gameManager, positionCard)
            return setCardFragmentInLayout(supportFragmentManager, frameId, carteFragment, isVisible)
        }
    }
}