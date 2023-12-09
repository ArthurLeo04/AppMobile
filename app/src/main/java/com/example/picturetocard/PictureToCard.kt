package com.example.picturetocard

import android.app.Application
import androidx.room.Room
import com.example.picturetocard.database.CardDatabase
import com.example.picturetocard.database.CardEntity
import com.example.picturetocard.game.Deck
import com.example.picturetocard.game.RuleManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PictureToCard : Application() {

    lateinit var ruleManager: RuleManager
    lateinit var database: CardDatabase

    override fun onCreate() {
        super.onCreate()

        // On initialise la base de donnée
        database = Room.databaseBuilder(applicationContext, CardDatabase::class.java, "card-database")
            .build()

        // Les règles du jeu
        ruleManager = RuleManager(database)


        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            Deck.insertTestDeck(database)
        }
    }
}