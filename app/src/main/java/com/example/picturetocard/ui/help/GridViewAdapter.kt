import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.picturetocard.game.Colors

class GridViewAdapter(private val data: Array<Array<String>>, private val context: Context, private val textSize: Float) :
    BaseAdapter() {

    override fun getCount(): Int {
        return (data.size + 1) * (data[0].size + 1)
    }

    override fun getItem(position: Int): Any {
        val row = position / (data[0].size + 1)
        val col = position % (data[0].size + 1)
        return if (row == 0 || col == 0) {
            Colors.values().getOrElse(row + col - 1) { Colors.FEU }
        } else {
            data[row - 1][col - 1]
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = position / (data[0].size + 1)
        val col = position % (data[0].size + 1)

        return if (row == 0 || col == 0) {
            getHeaderView(convertView, parent, row, col)
        } else {
            getCellView(convertView, parent, row - 1, col - 1)
        }
    }

    private fun getHeaderView(convertView: View?, parent: ViewGroup?, row: Int, col: Int): View {
        val imageView: ImageView = convertView as? ImageView ?: ImageView(context)

        val colorEnum = Colors.values().getOrElse(row + col - 1) { Colors.FEU }


        val svgRessourceId = Colors.getIdFromColor(colorEnum)
        val myIcon: Drawable? = context.getDrawable(svgRessourceId)

        imageView.setImageDrawable(myIcon)

        //imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        //imageView.layoutParams = AbsListView.LayoutParams(GridView.AUTO_FIT, AbsListView.LayoutParams.WRAP_CONTENT)
        //imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        //imageView.setBackgroundColor(getHeaderBackgroundColor())
        return imageView
    }

    private fun getCellView(convertView: View?, parent: ViewGroup?, row: Int, col: Int): View {
        val textView: TextView = convertView as? TextView ?: TextView(context)
        textView.text = data[row][col]
        textView.textSize = textSize
        textView.gravity = Gravity.CENTER // Centrer le texte dans la case
        return textView
    }



}