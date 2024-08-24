package com.example.playlistmaker.domain.repository

interface AudioPlayerRepository {
  fun playTrack(trackUrl: String)
  fun pauseTrack()
  fun stopTrack()
  fun isPlaying(): Boolean
  fun getCurrentPosition(): Int
  fun release()
  fun updateTrackProgress(callback: (currentTime: String) -> Unit)  //todo
  fun togglePlayback (trackUrl: String, onPlay: () -> Unit, onPause: () -> Unit)
}