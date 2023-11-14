import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.picturetocard.R
import com.example.picturetocard.game.Colors
import com.example.picturetocard.game.GameManager
import com.example.picturetocard.game.Hand
import com.example.picturetocard.game.getIdFromColor

class PlayerHandAdapter(private val data: GameManager, private val context: Context, private val textSize: Float) :
    BaseAdapter() {

    override fun getCount(): Int {
        return 2 * data.handPlayer.cards.size
    }

    private fun getResult(position: Int) : Int {
        val carte = data.cards.getCard(data.handPlayer.cards[position])
        if (carte != null) {
            data.matrice.getResult(carte.color, data.lastPlay.color)
        }
        return 0
    }

    override fun getItem(position: Int): Any {
        return if (position < data.handPlayer.cards.size) {
            // On récupére le text:
            getResult(position)
        } else {
            // On récupére la carte
            data.handPlayer.cards[position - data.handPlayer.cards.size]
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = position / (data.handPlayer.cards.size)
        val col = position % (data.handPlayer.cards.size)

        Log.d("Print","Appel de l'adaptateur de Hand")

        return if (row == 0) {
            getResultView(convertView, parent, row, col)
        } else {
            getCardView(convertView, parent, row - 1, col - 1)
        }
    }

    private fun getResultView(convertView: View?, parent: ViewGroup?, row: Int, col: Int): View {
        val textView: TextView = convertView as? TextView ?: TextView(context)
        textView.text = getResult(col).toString()
        textView.textSize = textSize
        textView.gravity = Gravity.CENTER // Centrer le texte dans la case
        return textView
    }

    private fun getCardView(convertView: View?, parent: ViewGroup?, row: Int, col: Int): View {
        val textView: TextView = convertView as? TextView ?: TextView(context)
        textView.text = getResult(col).toString()
        textView.textSize = textSize
        textView.gravity = Gravity.CENTER // Centrer le texte dans la case
        return textView // TODO : changer pour afficher la carte
    }

}