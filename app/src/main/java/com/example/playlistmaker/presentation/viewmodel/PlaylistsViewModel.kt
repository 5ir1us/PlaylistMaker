package com.example.playlistmaker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.TrackConverter
import com.example.playlistmaker.data.db.TrackInPlaylist
import com.example.playlistmaker.data.db.TrackInPlaylistDao
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.model.PlaylistModel
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val trackInPlaylistDao: TrackInPlaylistDao
) : ViewModel() {

    private val _playlists = MutableLiveData<List<PlaylistModel>>() // TODO:
    val playlists: LiveData<List<PlaylistModel>> = _playlists

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty


    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks



    init {
        loadPlaylists()
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlists ->
                _playlists.value = playlists
                _isEmpty.value = playlists.isEmpty()
            }
        }
    }


    fun loadTracksForPlaylist(playlistId: Long) {
        viewModelScope.launch {

            trackInPlaylistDao.getTracksInPlaylist(playlistId).collect { trackEntities ->
                val domainTracks = trackEntities
                    .map { TrackConverter.convertToDomainModel(it) }
                    .sortedByDescending { it.trackId }
                _tracks.postValue(domainTracks)

             }
        }
    }






}