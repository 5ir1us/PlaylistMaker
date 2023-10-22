package com.example.playlistmaker.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.utils.Constants

class AudioPlayerActivity : AppCompatActivity() {

  private lateinit var binding: ActivityAudioPlayerBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.apply {

      buttonPlayerBack.setOnClickListener {
        finish()
      }

      val intent = intent

      val image = intent.getStringExtra(Constants.PLAYER_IMAGE_TRACK)
      val time = intent.getStringExtra(Constants.PLAYER_TIME_TRACK)
      val collection = intent.getStringExtra(Constants.PLAYER_COLLECTION_TRACK)
      val year = intent.getStringExtra(Constants.PLAYER_YEAR_TRACK)
      val genre = intent.getStringExtra(Constants.PLAYER_GENRE_TRACK)
      val country = intent.getStringExtra(Constants.PLAYER_COUNTRY_TRACK)
      val artistName = intent.getStringExtra(Constants.PLAYER_ARTIST_NAME)
      val trackName = intent.getStringExtra(Constants.PLAYER_TRACK_NAME)

      Glide.with(this@AudioPlayerActivity)
        .load(image)
        .placeholder(R.drawable.placeholder)
        .into(imageAlbum)

      trackDuration.text = time //
      collectionName.text = collection //
      trackYear.text = year
      trackGenre.text = genre
      trackCountry.text = country
      viewArtistName.text = artistName
      albumArtist.text = trackName
      timeTrack.text = time

      if (collection == null) {
        visibleGroup.visibility = View.GONE
      }

    }
  }
}