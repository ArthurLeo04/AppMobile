package com.example.picturetocard

import android.app.Application
import androidx.room.Room
import com.example.picturetocard.database.CardDatabase
import com.example.picturetocard.game.RuleManager

class PictureToCard : Application() {

    lateinit var ruleManager: RuleManager
    companion object {
        lateinit var database: CardDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // On initialise la base de donnée
        database = Room.databaseBuilder(applicationContext, CardDatabase::class.java, "card-database")
            .build()

        // Les règles du jeu
        ruleManager = RuleManager(database)
    }
}