package com.example.picturetocard.ui.game

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.widget.Button
import android.widget.TextView
import com.example.picturetocard.R

class TableTypeDialog(context : Context) : Dialog(context) {
    init {
        this.setContentView(R.layout.table_types)

        val titre = this.findViewById<TextView>(R.id.titreView)
        val okButton = this.findViewById<Button>(R.id.btnOK)

        titre.setTypeface(null, Typeface.BOLD)

        okButton.setOnClickListener {
            this.dismiss() // Ferme le dialogue lorsque le bouton "OK" est cliqué
        }
    }
}