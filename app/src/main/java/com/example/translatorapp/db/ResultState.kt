package com.example.translatorapp.db

sealed class ResultState<out T> {
    object Loading:ResultState<Translate>()
    data class Succses<T>(val succses: T):ResultState<T>()
    data class Error(val error: Throwable):ResultState<Translate>()
}