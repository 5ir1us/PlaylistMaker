package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AudioPlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : AudioPlayerRepository {
    private var onTrackComplete: (() -> Unit)? = null
    private val handler = Handler(Looper.getMainLooper())
    private var currentPosition: Int = 0
    private var isPrepared: Boolean = false

    override fun playTrack(trackUrl: String) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(trackUrl)
            prepare()
            isPrepared = true
            setOnCompletionListener {
                onTrackComplete?.invoke()
                stopTrack()
            }
            start()
        }

        mediaPlayer.seekTo(currentPosition)
        mediaPlayer.start()

    }

    override fun pauseTrack() {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                currentPosition = mediaPlayer.currentPosition
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun stopTrack() {
        try {
            mediaPlayer.stop()
        } catch (e: IllegalStateException) {

        } finally {
            mediaPlayer.release()

        }
        currentPosition = 0
    }

    override fun isPlaying(): Boolean {
        return try {
            mediaPlayer.isPlaying && isPrepared
        } catch (e: IllegalStateException) {
            false
        }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition ?: 0
    }

    override fun release() {
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }

    override fun updateTrackProgress(): Flow<String> = flow {
        while (isPlaying()) {
            val currentPosition = getCurrentPosition() / 1000
            val minutes = currentPosition / 60
            val seconds = currentPosition % 60
            emit(String.format("%02d:%02d", minutes, seconds))
            delay(3000)
        }
        emit("00:00")
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