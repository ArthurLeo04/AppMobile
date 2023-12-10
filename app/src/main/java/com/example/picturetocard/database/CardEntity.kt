package com.example.picturetocard.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.picturetocard.game.Card
import com.example.picturetocard.game.Colors
import com.example.picturetocard.game.Effets

@Entity(tableName = "cards")
data class CardEntity (
    @ColumnInfo(name = "Couleur")
    var color : Int,
    @ColumnInfo(name = "Effet")
    var effet : Int,
    @ColumnInfo(name = "PathImage")
    var imagePath : String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun toCard() : Card {
        val res = Card(Colors.fromInt(color), Effets.fromInt(effet), id = id)
        if (imagePath.isNotEmpty()) res.setImageBitmap(imagePath) // On ajoute l'image
        return res
    }
}
