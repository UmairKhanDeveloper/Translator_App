package com.example.translatorapp.db.fav_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "simpleText")
    val simpleText: String,

    @ColumnInfo(name = "translateText")
    val translateText: String,

    @ColumnInfo(name = "languageCodeSimpleText1")
    val languageCodeSimpleText1: String = "",

    @ColumnInfo(name = "languageCodeTranslatedText1")
    val languageCodeTranslatedText1: String = ""
)
