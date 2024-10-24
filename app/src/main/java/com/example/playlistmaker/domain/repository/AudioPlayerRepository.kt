package com.example.playlistmaker.domain.repository

import kotlinx.coroutines.flow.Flow

interface AudioPlayerRepository {
  fun playTrack(trackUrl: String)
  fun pauseTrack()
  fun stopTrack()
  fun isPlaying(): Boolean
  fun getCurrentPosition(): Int
  fun release()
  fun trackProgress( ): Flow<String>  //todo
  fun togglePlayback (trackUrl: String, onPlay: () -> Unit, onPause: () -> Unit)
  fun setOnCompletionListener(listener: () -> Unit)

}