package com.example.picturetocard.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1,
    @ColumnInfo(name = "Couleur")
    var color : Int,
    @ColumnInfo(name = "Effet")
    var effet : Int,
    @ColumnInfo(name = "PathImage")
    var imagePath : String
)
