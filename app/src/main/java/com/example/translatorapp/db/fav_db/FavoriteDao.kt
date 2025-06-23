package com.example.translatorapp.db.fav_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM Favorite ORDER BY id DESC")
    fun getAllFavorite(): LiveData<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favorite(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)
}
