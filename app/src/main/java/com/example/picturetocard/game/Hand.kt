package com.example.picturetocard.game

import android.util.Log

class Hand(
    val cards: Array<Int>,
) {
    val isUse: Array<Boolean> = Array(cards.size) { false }
    val isVisible: Array<Boolean> = Array(cards.size) { false }

    public fun print() {
        val tag = "Print"
        Log.d(tag, cards.contentToString())
    }

    public fun play(id: Int) : Int {
        for (i in cards.indices) {
            if (cards[i] == id) {
                if (isUse[i] == true) {
                    return 0
                }
                isUse[i] = true
                isVisible[i] = true
                return cards[i]
            }
        }
        return 0
    }
}