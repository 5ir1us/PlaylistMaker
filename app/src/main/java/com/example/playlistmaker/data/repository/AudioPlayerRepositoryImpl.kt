package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.repository.AudioPlayerRepository

class AudioPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer?) : AudioPlayerRepository {
  private var onTrackComplete: (() -> Unit)? = null
  private val handler = Handler(Looper.getMainLooper())
  private var currentPosition: Int = 0

  override fun playTrack(trackUrl: String) {
    if (mediaPlayer == null) {
      mediaPlayer = MediaPlayer().apply {
        setDataSource(trackUrl)
        prepare()
        setOnCompletionListener {
          onTrackComplete?.invoke()
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
    try {
      mediaPlayer?.stop()
    } catch (e: IllegalStateException) {

    } finally {
      mediaPlayer?.release()
      mediaPlayer = null
    }
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
    handler.removeCallbacksAndMessages(null)
  }

  override fun updateTrackProgress(callback: (currentTime: String) -> Unit) {
    val runnable = object : Runnable {
      override fun run() {
        if (isPlaying()) {
          val currentPosition = getCurrentPosition() / 1000
          val minutes = currentPosition / 60
          val seconds = currentPosition % 60
          callback.invoke(String.format("%02d:%02d", minutes, seconds))
          handler.postDelayed(this, 1000)
        } else {
          handler.removeCallbacks(this)
          callback.invoke("00:00")
        }
      }
    }
    handler.post(runnable)
  }

  override fun togglePlayback(
    trackUrl: String,
    onPlay: () -> Unit,
    onPause: () -> Unit,
  ) {
    if (isPlaying()) {
      pauseTrack()
      onPause()
    } else {
      playTrack(trackUrl)
      onPlay()
    }
  }

  override fun setOnCompletionListener(listener: () -> Unit) {
    onTrackComplete = listener
  }
}