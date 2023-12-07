package com.example.picturetocard.game

import android.util.Log
import android.graphics.Bitmap

class Card(
    val color: Colors,
    val effet: Effets,
    //val positionInList: Int,
    val imageBitmap: Bitmap? = null
) {

    fun print() {
        Log.d("Print", "Carte : $color, $color")
    }
}