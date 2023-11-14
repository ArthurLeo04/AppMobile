package com.example.picturetocard.game

import android.util.Log

class Hand(
    public val cards: Array<Int>,
) {
    public var isUse: Array<Boolean> = Array(cards.size) { false }

    public fun print() {
        val tag = "Print"
        Log.d(tag, cards.contentToString())
    }
}