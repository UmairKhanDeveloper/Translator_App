package com.example.translatorapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Translate(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "simpleText")
    val simpleText: String,

    @ColumnInfo(name = "translateText")
    val translateText: String,

    @ColumnInfo(name = "languageCodeSimpleText")
    val languageCodeSimpleText: String,

    @ColumnInfo(name = "languageCodeTranslatedText")
    val languageCodeTranslatedText: String,


    )




