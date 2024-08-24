package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractor

class AudioPlayerInteractorImpl(private val trackPlayerRepository: AudioPlayerRepository) :
  AudioPlayerInteractor {

  override fun play(trackUrl: String) {
    trackPlayerRepository.playTrack(trackUrl)
  }

  override fun pause() {
    trackPlayerRepository.pauseTrack()
  }

  override fun isPlaying(): Boolean {
    return trackPlayerRepository.isPlaying()
  }

  override fun getCurrentPosition(): Int {
    return trackPlayerRepository.getCurrentPosition()
  }

  override fun stop() {
    trackPlayerRepository.stopTrack()
  }

  override fun release() {
    trackPlayerRepository.release()
  }

  override fun updateTrackProgress(callback: (currentTime: String) -> Unit) {
    trackPlayerRepository.updateTrackProgress(callback)
  }

  override fun togglePlayback(trackUrl: String, onPlay: () -> Unit, onPause: () -> Unit) {
    trackPlayerRepository.togglePlayback(trackUrl, onPlay, onPause)
  }
}