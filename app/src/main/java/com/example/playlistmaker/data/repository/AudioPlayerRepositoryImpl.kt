package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.repository.AudioPlayerRepository

class AudioPlayerRepositoryImpl (
  private val onTrackComplete: () -> Unit
) : AudioPlayerRepository {

  private var mediaPlayer: MediaPlayer? = null
  private var currentPosition: Int = 0

  override fun playTrack(trackUrl: String) {
    if (mediaPlayer == null) {
      mediaPlayer = MediaPlayer().apply {
        setDataSource(trackUrl)
        prepare()
        setOnCompletionListener {
          onTrackComplete()
          stopTrack()
        }
        start()
      }
    } else {
      mediaPlayer?.seekTo(currentPosition)
      mediaPlayer?.start()
    }
  }

  override fun pauseTrack() {
    mediaPlayer?.let {
      it.pause()
      currentPosition = it.currentPosition
    }
  }

  override fun stopTrack() {
    mediaPlayer?.release()
    mediaPlayer = null
    currentPosition = 0
  }

  override fun isPlaying(): Boolean {
    return mediaPlayer?.isPlaying ?: false
  }

  override fun getCurrentPosition(): Int {
    return mediaPlayer?.currentPosition ?: 0
  }

  override fun release() {
    mediaPlayer?.release()
    mediaPlayer = null
  }
}