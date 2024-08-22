package com.example.playlistmaker.presentation.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractor

class AudioPlayerActivity : AppCompatActivity() {

  companion object {
    const val TRACK_INFO: String = "Track"
  }

  private lateinit var binding: ActivityAudioPlayerBinding
  private lateinit var mediaPlayer: MediaPlayer
  private lateinit var handler: Handler
  private var isFavorite = false // избранное состояние
  private lateinit var trackPlayerInteractor: AudioPlayerInteractor//todo

  @RequiresApi(VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.apply {

      trackPlayerInteractor = Creator.providePlayTrackInteractor(
        onTrackComplete = { binding.playButton.setImageResource(R.drawable.play_button) }
      )
      buttonPlayerBack.setOnClickListener {
        finish()
      }

      val track = intent.getParcelableExtra<Track>(TRACK_INFO)
      //не совместим с 26 мин вер. нужна 33
      // val track = intent.getParcelableExtra("track",Track::class.java)

      val radiusImage = dpToPx(8f, this@AudioPlayerActivity)

      Glide.with(this@AudioPlayerActivity)
        .load(track?.getCoverArtwork())
        .placeholder(R.drawable.placeholder)
        .centerCrop()
        .transform(RoundedCorners(radiusImage))
        .into(imageAlbum)

// Заполнение UI данными трека
      track?.apply {
        trackDuration.text = getTimeTrack()
        albumOneName.text = collectionName
        trackYear.text = changeDateFormat()
        trackGenre.text = primaryGenreName
        trackCountry.text = country
        viewArtistName.text = artistName
        albumArtist.text = trackName
        timeTrack.text = getTimeTrack()
        visibleGroup.isVisible = collectionName != null
      }

      //todo new
      playButton.setOnClickListener {
        togglePlayback(track?.previewUrl ?: "")
      }

      //   обработчик на кнопку воспроизведения
      favoriteMusic.setOnClickListener { toggleFavorite() }

      // Инициализация Handler
      handler = Handler(Looper.getMainLooper())

      // Инициализация MediaPlayer
      mediaPlayer = MediaPlayer().apply {
        setDataSource(track?.previewUrl) // Установка ссылки на аудиофайл
        prepare() // Подготовка MediaPlayer к воспроизведению
      }
    }
  }

  // из цикла жизни приложения
  override fun onPause() {
    super.onPause()
    if (trackPlayerInteractor.isPlaying()) {
      trackPlayerInteractor.pause()
    }
  }

  private fun togglePlayback(trackUrl: String) {
    if (trackPlayerInteractor.isPlaying()) {
      trackPlayerInteractor.pause()
      binding.playButton.setImageResource(R.drawable.play_button)
    } else {
      trackPlayerInteractor.play(trackUrl)
      binding.playButton.setImageResource(R.drawable.stop_player)
      updateTrackProgress()
    }
  }

  private fun toggleFavorite() {
    isFavorite = !isFavorite // Меняем состояние на противоположное
    updateFavoriteIcon() // Обновляем иконку
  }

  private fun updateFavoriteIcon() {
    if (isFavorite) {
      binding.favoriteMusic.setImageResource(R.drawable.like_button) //   иконк   избранное
    } else {
      binding.favoriteMusic.setImageResource(R.drawable.favorite_border) // иконкуа  не избранное
    }
  }

  // Метод для обновления текущего времени воспроизведения
  private fun updateTrackProgress() {
    val runnable = object : Runnable {
      override fun run() {
        if (trackPlayerInteractor.isPlaying()) {
          val currentPosition = trackPlayerInteractor.getCurrentPosition() / 1000
          val minutes = currentPosition / 60
          val seconds = currentPosition % 60
          binding.timeTrack.text = String.format("%02d:%02d", minutes, seconds)
          handler.postDelayed(this, 1000) // Обновляем каждую секунду
        } else {
          handler.removeCallbacks(this)
          // binding.playButton.setImageResource(R.drawable.play_button)
          binding.timeTrack.text = "00:00"
          handler.removeCallbacks(this)
        }
      }
    }
    handler.post(runnable)
  }

  // Освобождение ресурсов при завершении активности
  override fun onDestroy() {
    super.onDestroy()
    trackPlayerInteractor.release()
    handler.removeCallbacksAndMessages(null) // Останавливаем обновление времени
  }

  private fun dpToPx(
    dp: Float,
    context: Context,
  ): Int {
    return TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP,
      dp,
      context.resources.displayMetrics
    ).toInt()
  }
}