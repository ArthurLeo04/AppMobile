package com.example.picturetocard

import android.app.Application
import com.example.picturetocard.game.GameManager
import com.example.picturetocard.game.RuleManager

class PictureToCard : Application() {
    val ruleManager : RuleManager = RuleManager()

}