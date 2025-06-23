package com.example.translatorapp.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
class MainViewModel(private val repository: Repository) : ViewModel() {

    val allTranslate: LiveData<List<Translate>> = repository.getAllNotes()

    fun insert(translate: Translate) {
        viewModelScope.launch {
            repository.insert(translate)
        }
    }

    fun update(translate: Translate) {
        viewModelScope.launch {
            repository.update(translate)
        }
    }

    fun delete(translate: Translate) {
        viewModelScope.launch {
            repository.delete(translate)
        }
    }
}
