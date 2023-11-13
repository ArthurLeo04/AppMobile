import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView

class GridViewAdapter(private val data: Array<Array<String>>, private val context: Context, private val textSize: Float) :
    BaseAdapter() {

    override fun getCount(): Int {
        return data.size * data[0].size
    }

    override fun getItem(position: Int): Any {
        val row = position / data[0].size
        val col = position % data[0].size
        return data[row][col]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView: TextView = convertView as? TextView ?: TextView(context)

        val row = position / data[0].size
        val col = position % data[0].size

        textView.text = data[row][col]
        textView.textSize = textSize
        textView.gravity = Gravity.CENTER // Centrer le texte dans la case

        return textView
    }
}