package com.example.picturetocard.game

import android.graphics.Bitmap
import android.os.Parcelable
import android.os.Parcel

class Card(
    val color: Colors,
    val effet: Effets,
    val imageBitmap: Bitmap? = null
)