package com.example.translatorapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Translate::class], version = 2, exportSchema = false)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun getDao(): TranslateDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDataBase? = null

        fun getDataBase(context: Context): NoteDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, NoteDataBase::class.java,
                    "notes_database"
                ).build()
                INSTANCE = instance
                instance
            }

        }

    }
}