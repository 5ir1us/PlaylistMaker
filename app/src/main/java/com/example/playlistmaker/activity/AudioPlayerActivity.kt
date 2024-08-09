package com.example.playlistmaker.activity

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
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.utils.Constants

class AudioPlayerActivity : AppCompatActivity() {

  companion object {
    const val TRACK_INFO: String = "Track"
  }

  private lateinit var binding: ActivityAudioPlayerBinding
  private lateinit var mediaPlayer: MediaPlayer
  private lateinit var handler: Handler
  private var isPlaying = false //   для отслеживания состояния
  private var track: Track? = null // Переменная для хранения данных трека

  @RequiresApi(VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.apply {

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

      playButton.setOnClickListener { togglePlayback() }

      //   обработчик на кнопку воспроизведения
      addTrack.setOnClickListener { togglePlayback() }

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
    if (isPlaying) {
      pauseTrack() // Останавливаем воспроизведение, когда активность переходит в фоновый режим
    }
  }

  // Метод для переключения воспроизведения и паузы
  private fun togglePlayback() {
    if (isPlaying) {
      pauseTrack()
    } else {
      playTrack()
    }
  }

  // Метод для воспроизведения трека
  private fun playTrack() {
    mediaPlayer.start() // Начинаем воспроизведение
    isPlaying = true
    binding.playButton.setImageResource(R.drawable.stop_player) // Меняем иконку на "Пауза"
    updateTrackProgress() // Обновляем прогресс трека
  }

  // Метод для паузы воспроизведения
  private fun pauseTrack() {
    mediaPlayer.pause() // Приостанавливаем воспроизведение
    isPlaying = false
    binding.playButton.setImageResource(
      R.drawable.play_button
    ) // Меняем иконку на "Воспроизведение"
  }

  // Метод для обновления текущего времени воспроизведения
  private fun updateTrackProgress() {
    val runnable = object : Runnable {
      override fun run() {
        if (mediaPlayer.isPlaying) {
          val currentPosition = mediaPlayer.currentPosition / 1000
          val minutes = currentPosition / 60
          val seconds = currentPosition % 60
          binding.timeTrack.text = String.format("%02d:%02d", minutes, seconds)
          handler.postDelayed(this, 1000) // Обновляем каждую секунду
        }else{
          binding.playButton.setImageResource(R.drawable.play_button) // Время закончилось, возвращаем кнопку в состояние "Играть"
          binding.timeTrack.text = "00:00"
        }
      }
    }
    handler.post(runnable)
  }

  // Освобождение ресурсов при завершении активности
  override fun onDestroy() {
    super.onDestroy()
    mediaPlayer.release() // Освобождаем ресурсы MediaPlayer
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