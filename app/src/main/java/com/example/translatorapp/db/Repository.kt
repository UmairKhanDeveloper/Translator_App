package com.example.translatorapp.db

import androidx.lifecycle.LiveData

class Repository(private val translateDataBase: NoteDataBase) {

    fun getAllNotes(): LiveData<List<Translate>> = translateDataBase.getDao().getAllTranslate()



    fun insert(translate: Translate) {
        translateDataBase.getDao().insert(translate)
    }

    suspend fun update(translate: Translate) {
        translateDataBase.getDao().update(translate)
    }

    suspend fun delete(translate: Translate) {
        translateDataBase.getDao().delete(translate)
    }
}
