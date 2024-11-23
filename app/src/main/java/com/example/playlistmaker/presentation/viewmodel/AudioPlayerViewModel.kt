package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.state.AudioPlayerState
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
  private val audioPlayerInteractor: AudioPlayerInteractor,
  private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {


  private val _isFavorite = MutableLiveData<Boolean>()
  val isFavorite: LiveData<Boolean> = _isFavorite

  private val _screenState = MutableLiveData<AudioPlayerState>()
  val screenState: LiveData<AudioPlayerState> get() = _screenState

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
    _screenState.value = _screenState.value?.copy(isPlaying = false,currentTrackTime = "00:00")
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

  // TODO:
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
  // TODO:  

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




}
