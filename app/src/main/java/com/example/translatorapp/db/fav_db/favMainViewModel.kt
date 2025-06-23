package com.example.translatorapp.db.fav_db

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class FavMainViewModel(private val favRepository: FavRepository) : ViewModel() {

    val allFavorite: LiveData<List<Favorite>> = favRepository.allFavorite()

    fun favorite(favorite: Favorite) {
        viewModelScope.launch {
            favRepository.favorite(favorite)
        }
    }
    fun delete(favorite: Favorite){
        viewModelScope.launch {
            favRepository.delete(favorite)
        }
    }
}
