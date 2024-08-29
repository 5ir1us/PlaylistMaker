package com.example.playlistmaker.presentation.ui.activity

import android.content.Context
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.presentation.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.presentation.viewmodel.AudioPlayerViewModelFactory

class AudioPlayerActivity : AppCompatActivity() {

  companion object {
    const val TRACK_INFO: String = "Track"
  }

  private lateinit var binding: ActivityAudioPlayerBinding

  private val audioPlayerViewModel: AudioPlayerViewModel by viewModels {
    Creator.provideAudioPlayerViewModelFactory( )
  }

  @RequiresApi(VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val track = intent.getParcelableExtra<Track>(TRACK_INFO)
    track?.let { setupTrackInfo(it) }
    observeViewModel()

    binding.playButton.setOnClickListener {
      track?.let { track ->
        track.previewUrl?.let { url ->
          audioPlayerViewModel.togglePlayback(url)
        }
      }
    }

    binding.favoriteMusic.setOnClickListener {
      audioPlayerViewModel.toggleFavorite()

    }

    binding.buttonPlayerBack.setOnClickListener {
      finish()
    }
  }


  private fun observeViewModel() {
    audioPlayerViewModel.screenState.observe(this@AudioPlayerActivity) { state ->
      binding.timeTrack.text = state.currentTrackTime
      binding.playButton.setImageResource(
        if (state.isPlaying) R.drawable.stop_player else R.drawable.play_button
      )
      binding.favoriteMusic.setImageResource(
        if (state.isFavorite) R.drawable.like_button else R.drawable.favorite_border
      )
    }
  }

  private fun setupTrackInfo(track: Track) {
    val radiusImage = dpToPx(8f, this)
    Glide.with(this)
      .load(track.getCoverArtwork())
      .placeholder(R.drawable.placeholder)
      .centerCrop()
      .transform(RoundedCorners(radiusImage))
      .into(binding.imageAlbum)

    binding.trackDuration.text = track.getTimeTrack()
    binding.albumOneName.text = track.collectionName
    binding.trackYear.text = track.changeDateFormat()
    binding.trackGenre.text = track.primaryGenreName
    binding.trackCountry.text = track.country
    binding.viewArtistName.text = track.artistName
    binding.albumArtist.text = track.trackName
    binding.timeTrack.text = "00:00"   // track.getTimeTrack()
    binding.visibleGroup.isVisible = track.collectionName != null
  }

  //Жизненный цикл Activiti
  override fun onPause() {
    super.onPause()
    audioPlayerViewModel.pauseTrack()
  }

  //Жизненый цикл Activiti
  override fun onDestroy() {
    super.onDestroy()
    audioPlayerViewModel.stopTrack()
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