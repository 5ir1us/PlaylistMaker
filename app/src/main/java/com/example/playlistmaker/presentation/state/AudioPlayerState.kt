package com.example.playlistmaker.presentation.state

data class AudioPlayerState (val isPlaying: Boolean = false,
  val currentTrackTime: String = "00:00",
  val isFavorite: Boolean = false)