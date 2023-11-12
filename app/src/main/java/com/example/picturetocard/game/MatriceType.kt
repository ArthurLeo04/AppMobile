package com.example.picturetocard.game

import android.content.Context
import android.content.res.AssetManager
import com.example.picturetocard.R
import java.io.File

class MatriceType(
    public val matrice : Array<Array<Int>>
) {
    init {
        // Verifie que la matrice a la bonne taille
        require(matrice.size == Colors.values().size)
        require(matrice.all { it.size == Colors.values().size })
    }


    companion object { // Initialise la matrice à partir d'un fichier
        fun fromFile(filePath: String): MatriceType {
            //val file = context.assets.open(filePath).reader()
            //val testLines = getRessources().getString(R.string.matrix)

            val file = File(filePath)
            val lines = file.readLines()

            val matrice = Array(lines.size) { Array(lines.size) { 0 } }

            for (i in lines.indices) {
                val values = lines[i].split(",")
                for (j in values.indices) {
                    matrice[i][j] = values[j].toInt()
                }
            }

            return MatriceType(matrice)
        }

        fun createDefault(): MatriceType {
            val matrice = arrayOf(
                arrayOf(+0,-2,+2,+0,+2,-1,+1,-1), // Feu
                arrayOf(+2,+0,-2,-2,+0,+1,+1,-1), // Eau
                arrayOf(-2,+2,+0,-1,-1,+2,-2,+1), // Nature
                arrayOf(+0,+2,+1,+0,+0,-2,+2,-2), // Foudre
                arrayOf(-2,+0,+1,+0,+2,+2,+0,+0), // Glace
                arrayOf(+1,-1,-2,+2,-2,+0,-1,+2), // Roche
                arrayOf(-1,-1,+2,-2,+0,+1,+0,+0), // Metal
                arrayOf(+1,+1,-1,+2,+0,-2,+0,+0), // Air
            )
            return MatriceType(matrice)
        }
    }

    fun getResult(color1 : Colors, color2 : Colors) : Int {
        // Retourne le résultat en ayant joué color1 sur color2
        val index1 = color1.ordinal
        val index2 = color2.ordinal
        return matrice[index1][index2]
    }

    fun printMatrix() {
        val colors = Colors.values()
        val headerRow = colors.joinToString("\t", "\t", "\n") { it.name.take(3) }

        val rows = colors.mapIndexed { rowIndex, color ->
            val values = matrice[rowIndex].joinToString("\t") { it.toString() }
            "${color.name.take(3)}\t$values"
        }

        println(headerRow + rows.joinToString("\n"))
    }
}