package com.example.picturetocard.game

import com.example.picturetocard.R

enum class Effets {
    FEU, // Rouge
    EAU, // Cyan ou Bleu
    NATURE, // Vert
    FOUDRE, // Jaune
    GLACE, // Blanc
    ROCHE, // Marron
    METAL, // Gris
    AIR, // Mauve
    PLUS_UN, // +1
    DOUBLE_ACT // *2
    ;

    companion object {
        fun fromInt(value: Int): Effets {
            return Effets.values().first { it.ordinal == value }
        }

        fun getRandom() : Effets {
            return Effets.values().random()
        }

        fun getIdFromEffet(effetEnum: Effets): Int {
            return when (effetEnum) {
                Effets.FEU -> R.drawable.feu
                Effets.EAU -> R.drawable.eau
                Effets.NATURE -> R.drawable.nature
                Effets.FOUDRE -> R.drawable.foudre
                Effets.GLACE -> R.drawable.glace
                Effets.METAL -> R.drawable.metal
                Effets.ROCHE -> R.drawable.roche
                Effets.AIR -> R.drawable.air
                Effets.PLUS_UN -> R.drawable.plusone
                Effets.DOUBLE_ACT -> R.drawable.copy
            }
        }
    }
}
