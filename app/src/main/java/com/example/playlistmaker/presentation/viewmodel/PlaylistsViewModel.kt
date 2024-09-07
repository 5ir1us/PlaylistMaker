package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.SearchTrackRepository
import kotlinx.coroutines.launch

class PlaylistsViewModel (
    private val playlistsRepository: SearchTrackRepository
) : ViewModel() {

    private val _playlists = MutableLiveData<List<Track>>()
    val playlists: LiveData<List<Track>> = _playlists

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            _playlists.value   }
    }
}