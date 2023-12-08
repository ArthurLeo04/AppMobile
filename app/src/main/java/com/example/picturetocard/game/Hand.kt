package com.example.picturetocard.game

import android.util.Log

class Hand( // Stocke les positions dans la liste globale
    val cards: Array<Int>,
) {
    var isUse: Array<Boolean> = Array(cards.size) { false }
    val isVisible: Array<Boolean> = Array(cards.size) { false }

    fun print() {
        val tag = "Print"
        Log.d(tag, cards.contentToString())
    }


    fun play(index: Int, doubleEffet : Boolean) : Boolean {
        if (isUse[index]) return false
        if (!doubleEffet) isUse[index] = true
        isVisible[index] = true
        return true
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

    fun getByPosition(position: Int): Int {
        return cards.indexOf(position)
    }
}