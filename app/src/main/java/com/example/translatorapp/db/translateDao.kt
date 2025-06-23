package com.example.translatorapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TranslateDao {

    @Query("SELECT * FROM Translate ORDER BY id DESC")
    fun getAllTranslate(): LiveData<List<Translate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(translate: Translate)

    @Update
    suspend fun update(translate: Translate)

    @Delete
    suspend fun delete(translate: Translate)
}
