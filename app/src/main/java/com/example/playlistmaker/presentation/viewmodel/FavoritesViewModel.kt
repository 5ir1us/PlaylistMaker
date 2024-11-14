package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.SearchTrackRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor) :
    ViewModel() {

    // LiveData для отслеживания списка избранных треков
    val favoriteTracks: LiveData<List<Track>> =
        favoriteTracksInteractor.getAllFavoriteTracks().asLiveData()

    // Метод для добавления трека в избранное
    fun addTrackToFavorites(track: Track) {
        viewModelScope.launch {
            favoriteTracksInteractor.addTrackToFavorites(track)
        }
    }

    // Метод для удаления трека из избранного
    fun removeTrackFromFavorites(track: Track) {
        viewModelScope.launch {
            favoriteTracksInteractor.removeTrackFromFavorites(track)
        }
    }


}