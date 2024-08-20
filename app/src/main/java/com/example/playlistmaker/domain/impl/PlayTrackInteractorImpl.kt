package com.example.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayTrackInteractor


class PlayTrackInteractorImpl(private val onTrackComplete: () -> Unit) : PlayTrackInteractor {

  private var mediaPlayer: MediaPlayer? = null
  private var currentPosition: Int = 0

  override fun play(trackUrl: String) {
    if (mediaPlayer == null) {
      mediaPlayer = MediaPlayer().apply {
        setDataSource(trackUrl)
        prepare()
        setOnCompletionListener {
          onTrackComplete()
          stop()
        }
        start()
      }
    } else {
      mediaPlayer?.seekTo(currentPosition)
      mediaPlayer?.start()
    }
  }

  override fun pause() {
    mediaPlayer?.let {
      it.pause()
      currentPosition = it.currentPosition
      onTrackComplete()
    }
  }

  override fun isPlaying(): Boolean {
    return mediaPlayer?.isPlaying ?: false
  }

  override fun getCurrentPosition(): Int {
    return mediaPlayer?.currentPosition ?: 0
  }

  override fun stop() {
    mediaPlayer?.release()
    mediaPlayer = null
    currentPosition = 0
  }

  override fun release() {
    mediaPlayer?.release()
    mediaPlayer = null
  }
}