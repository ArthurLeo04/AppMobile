package com.example.picturetocard.game

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel(

) {
    lateinit var hand1 : Hand
    lateinit var hand2 : Hand

    var score : Int = 0

    var matrice = MatriceType.createDefault()
}
