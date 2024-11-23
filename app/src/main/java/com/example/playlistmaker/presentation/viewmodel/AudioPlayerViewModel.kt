package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.model.PlaylistModel
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.state.AudioPlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {


    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _screenState = MutableLiveData<AudioPlayerState>()
    val screenState: LiveData<AudioPlayerState> get() = _screenState

    private val _playlists = MutableLiveData<List<PlaylistModel>>()
     val playlists: LiveData<List<PlaylistModel>> = _playlists


    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _addTrackStatus = MutableLiveData<Boolean>()
    val addTrackStatus: LiveData<Boolean> get() = _addTrackStatus


    init {
        _screenState.value = AudioPlayerState(
            isPlaying = audioPlayerInteractor.isPlaying(),
            isFavorite = false
        )

        audioPlayerInteractor.setOnTrackCompleteListener {
            onTrackComplete()
        }
    }


    fun pauseTrack() {
        audioPlayerInteractor.pause()
        updateState(isPlaying = false)
    }

    fun stopTrack() {
        audioPlayerInteractor.stop()
        updateState(isPlaying = false, currentTrackTime = "00:00")
    }

    private fun onTrackComplete() {
        _screenState.value = _screenState.value?.copy(isPlaying = false, currentTrackTime = "00:00")
    }

    fun togglePlayback(trackUrl: String) {
        audioPlayerInteractor.togglePlayback(
            trackUrl,
            onPlay = {
                updateState(isPlaying = true)
                updateTrackProgress()
            },
            onPause = {
                updateState(isPlaying = false)
            }
        )
    }


    fun toggleFavorite(track: Track) {
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                favoriteTracksInteractor.removeTrackFromFavorites(track)
                _isFavorite.value = false
            } else {
                favoriteTracksInteractor.addTrackToFavorites(track)
                _isFavorite.value = true
            }
            updateState(isFavorite = _isFavorite.value) // Обновляем UI состояние
        }
    }

    fun checkIfFavorite(track: Track) {
        viewModelScope.launch {
            favoriteTracksInteractor.getAllFavoriteTracks().collect { favoriteTracks ->
                val isFav = favoriteTracks.any { it.trackId == track.trackId }
                _isFavorite.value = isFav
                updateState(isFavorite = isFav)
            }
        }
    }


    private fun updateTrackProgress() {
        viewModelScope.launch {
            audioPlayerInteractor.updateTrackProgress().collect { time ->
                updateState(currentTrackTime = time)
            }
        }
    }

    private fun updateState(
        isPlaying: Boolean? = null,
        currentTrackTime: String? = null,
        isFavorite: Boolean? = null,
    ) {
        _screenState.value = _screenState.value?.copy(
            isPlaying = isPlaying ?: _screenState.value!!.isPlaying,
            currentTrackTime = currentTrackTime ?: _screenState.value!!.currentTrackTime,
            isFavorite = isFavorite ?: _screenState.value!!.isFavorite
        )
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.release()
    }

    fun getPlaylists(): Flow<List<PlaylistModel>> {
        return playlistInteractor.getAllPlaylists()
    }

    fun addTrackToPlaylist(track: Track, playlist: PlaylistModel) {
        viewModelScope.launch {
            val isTrackAlreadyInPlaylist =
                playlistInteractor.isTrackInPlaylist(track.trackId, playlist.id)

            if (!isTrackAlreadyInPlaylist) {
                playlistInteractor.addTrackToPlaylist(track, playlist)
                _addTrackStatus.postValue(true)

                loadPlaylists()
            } else {
                _addTrackStatus.postValue(false)
            }
        }
    }

    // Метод для загрузки плейлистов и обновления LiveData
    private fun loadPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { updatedPlaylists ->
                _playlists.value = updatedPlaylists
                _isEmpty.value = updatedPlaylists.isEmpty()
            }
        }
    }

    suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean {
        return playlistInteractor.isTrackInPlaylist(trackId, playlistId)
    }
}
