package com.example.picturetocard.database

class CardComparator : Comparator<CardEntity> {

    override fun compare(p0: CardEntity, p1: CardEntity): Int { // Compare deux cartes
        val firstComparison = p0.color.compareTo(p1.color)
        return if (firstComparison != 0) {
            firstComparison
        } else {
            p0.effet.compareTo(p1.effet)
        }
    }
}