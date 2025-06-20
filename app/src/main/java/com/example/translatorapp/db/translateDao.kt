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
    fun insert(translate: Translate)

    @Update
    fun update(translate: Translate)

    @Delete
    fun delete(translate: Translate)
}
