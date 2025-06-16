package com.example.texttranslater.domain.model.language


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class LanguagesItem(
    @SerialName("code")
    val code: String?,
    @SerialName("flag")
    val flag: String?,
    @SerialName("isSelected")
    val isSelected: Boolean = false,
    @SerialName("localName")
    val localName: String?,
    @SerialName("name")
    val name: String?
)