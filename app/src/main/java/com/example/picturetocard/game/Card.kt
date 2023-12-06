package com.example.picturetocard.game

import android.util.Log
import android.graphics.Bitmap

class Card(
    val color: Colors,
    val effet: Effets,
    val imageBitmap: Bitmap? = null
) {
    var id : Int = 0

    fun print() {
        Log.d("Print", "Carte : $color, $color")
    }
}