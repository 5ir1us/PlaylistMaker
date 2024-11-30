package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor) :
    ViewModel() {

     val favoriteTracks: LiveData<List<Track>> =
        favoriteTracksInteractor.getAllFavoriteTracks().asLiveData()

     fun addTrackToFavorites(track: Track) {
        viewModelScope.launch {
            favoriteTracksInteractor.addTrackToFavorites(track)
        }
    }

     fun removeTrackFromFavorites(track: Track) {
        viewModelScope.launch {
            favoriteTracksInteractor.removeTrackFromFavorites(track)
        }
    }



}