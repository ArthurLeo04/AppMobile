package com.example.picturetocard.game

import android.util.Log

class Hand(
    val cards: Array<Int>,
) {
    var isUse: Array<Boolean> = Array(cards.size) { false }
    val isVisible: Array<Boolean> = Array(cards.size) { false }

    fun print() {
        val tag = "Print"
        Log.d(tag, cards.contentToString())
    }


    fun play(id: Int) : Int {
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

    fun isEmpty() : Boolean {
        for (used in isUse) {
            if (!used) return false
        }
        return true
    }

    fun UnUseCards() {
        isUse = Array(cards.size) { false }
    }
}