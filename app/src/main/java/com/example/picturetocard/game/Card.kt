package com.example.picturetocard.game

import android.util.Log
import kotlin.properties.Delegates
import android.graphics.Bitmap

class Card(

    public val color: Colors,
    public val effet: Effets,
    public val imageBitmap: Bitmap? = null
) {
    var id by Delegates.notNull<Int>()

    companion object {
        private var nextId = 1

    }

    init {
        id = nextId++
    }

    public fun print() {
        Log.d("Print", "Carte : $id, $color, $color")
    }
}