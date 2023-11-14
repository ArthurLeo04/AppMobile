package com.example.picturetocard

import android.app.Application
import com.example.picturetocard.game.GameManager

class PictureToCard : Application() {
    val gameManager : GameManager = GameManager()

}