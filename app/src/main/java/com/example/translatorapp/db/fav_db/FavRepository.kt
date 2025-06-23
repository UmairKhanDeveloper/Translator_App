package com.example.translatorapp.db.fav_db

import androidx.lifecycle.LiveData
import com.example.translatorapp.db.Translate


class FavRepository(private val db: FavoriteDatabase) {

    fun allFavorite(): LiveData<List<Favorite>> =
        db.getFavoriteDao().getAllFavorite()

    suspend fun favorite(favorite: Favorite) {
        db.getFavoriteDao().favorite(favorite)
    }

    suspend fun delete(favorite: Favorite){
        db.getFavoriteDao().delete(favorite)
    }
}


