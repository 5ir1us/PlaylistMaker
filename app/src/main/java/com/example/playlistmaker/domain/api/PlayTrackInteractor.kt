package com.example.playlistmaker.domain.api

interface PlayTrackInteractor {
  fun play(trackUrl: String)
  fun pause()
  fun isPlaying(): Boolean
  fun getCurrentPosition(): Int
  fun stop()
  fun release()
}