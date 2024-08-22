package com.example.playlistmaker.domain.interactor

interface AudioPlayerInteractor {
  fun play(trackUrl: String)
  fun pause()
  fun isPlaying(): Boolean
  fun getCurrentPosition(): Int
  fun stop()
  fun release()
}