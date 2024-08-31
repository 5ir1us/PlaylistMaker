package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.presentation.state.AudioPlayerState

class AudioPlayerViewModel(
  private val audioPlayerInteractor: AudioPlayerInteractor,
) : ViewModel() {

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

  fun playTrack(trackUrl: String) {
    audioPlayerInteractor.play(trackUrl)
    updateState(isPlaying = true)
    updateTrackProgress()
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
    _screenState.value = _screenState.value?.copy(isPlaying = false)
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

  fun toggleFavorite() {
    _screenState.value =
      _screenState.value?.copy(isFavorite = !(_screenState.value?.isFavorite ?: false))
  }

  private fun updateTrackProgress() {
    audioPlayerInteractor.updateTrackProgress { time ->
      updateState(currentTrackTime = time)
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
