package com.example.playlistmaker.domain.interactor

import kotlinx.coroutines.flow.Flow

interface AudioPlayerInteractor {
  fun play(trackUrl: String)
  fun pause()
  fun isPlaying(): Boolean
  fun getCurrentPosition(): Int
  fun stop()
  fun release()
  fun updateTrackProgress(): Flow<String>
  fun togglePlayback (trackUrl: String, onPlay: () -> Unit, onPause: () -> Unit)
  fun setOnTrackCompleteListener(listener: () -> Unit)
  }