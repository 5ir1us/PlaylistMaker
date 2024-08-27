package com.example.playlistmaker.presentation.ui

import android.content.Context
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
  private var isFavorite = false // избранное состояние
  private lateinit var trackPlayerInteractor: AudioPlayerInteractor

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

      playButton.setOnClickListener {
        trackPlayerInteractor.togglePlayback(
          track?.previewUrl ?: "",
          onPlay = {
            binding.playButton.setImageResource(R.drawable.stop_player)
            updateTrackProgress()
          },
          onPause = {
            binding.playButton.setImageResource(R.drawable.play_button)
          }
        )
      }

      //   обработчик на кнопку воспроизведения
      favoriteMusic.setOnClickListener { toggleFavorite() }
    }
    updateTrackProgress()
  }

  //Жизненный цикл Activiti
  override fun onPause() {
    super.onPause()
    if (trackPlayerInteractor.isPlaying()) {
      trackPlayerInteractor.pause()
    }
  }

  private fun toggleFavorite() {
    isFavorite = !isFavorite // Меняем состояние на противоположное
    updateFavoriteIcon()
  }

  private fun updateFavoriteIcon() {
    if (isFavorite) {
      binding.favoriteMusic.setImageResource(R.drawable.like_button) //   иконк   избранное
    } else {
      binding.favoriteMusic.setImageResource(R.drawable.favorite_border) // иконкуа  не избранное
    }
  }

  private fun updateTrackProgress() {
    trackPlayerInteractor.updateTrackProgress { formattedTime ->
      binding.timeTrack.text = formattedTime
    }
  }


  //todo перенеси вверх для читаемости
  // Жизненный цикл Activiti
  override fun onDestroy() {
    super.onDestroy()
    trackPlayerInteractor.release()
  }

  //Жизненный цикл Activiti
  override fun onResume() {
    super.onResume()
    if (trackPlayerInteractor.isPlaying()) {

      binding.playButton.setImageResource(R.drawable.stop_player)

      updateTrackProgress()
    } else {
      binding.playButton.setImageResource(R.drawable.play_button)
    }
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