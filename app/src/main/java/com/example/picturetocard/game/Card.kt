package com.example.picturetocard.game

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcelable
import android.os.Parcel
import java.io.File

class Card(
    val color: Colors,
    val effet: Effets,
    var imageBitmap: Bitmap? = null,
    val id : Int = 0
) {
    fun setImageBitmap(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            imageBitmap = BitmapFactory.decodeFile(file.absolutePath, options)
        }
    }
}
