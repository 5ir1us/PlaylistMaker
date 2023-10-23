package com.example.playlistmaker.activity

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
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.utils.Constants

class AudioPlayerActivity : AppCompatActivity() {

  companion object {
    const val TRACK_INFO: String = "Track"
  }

  private lateinit var binding: ActivityAudioPlayerBinding

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