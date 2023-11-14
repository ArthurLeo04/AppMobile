package com.example.picturetocard.game

import com.example.picturetocard.R

enum class Colors {
    FEU, // Rouge
    EAU, // Cyan ou Bleu
    NATURE, // Vert
    FOUDRE, // Jaune
    GLACE, // Blanc
    ROCHE, // Marron
    METAL, // Gris
    AIR // Mauve
    // Noir ?
}

public fun getIdFromColor(colorEnum: Colors): Int {
    return when (colorEnum) {
        Colors.FEU -> R.drawable.feu
        Colors.EAU -> R.drawable.eau
        Colors.NATURE -> R.drawable.nature
        Colors.FOUDRE -> R.drawable.foudre
        Colors.GLACE -> R.drawable.glace
        Colors.METAL -> R.drawable.metal
        Colors.ROCHE -> R.drawable.roche
        Colors.AIR -> R.drawable.air
    }
}

public fun getStyleFromColor(colorEnum: Colors): Int {
    return when (colorEnum) {
        Colors.FEU -> R.color.feu
        Colors.EAU -> R.color.eau
        Colors.NATURE -> R.color.nature
        Colors.FOUDRE -> R.color.foudre
        Colors.GLACE -> R.color.glace
        Colors.METAL -> R.color.metal
        Colors.ROCHE -> R.color.roche
        Colors.AIR -> R.color.air
    }
}