package com.example.playlistmaker.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.databinding.LayoutTrackBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(item: View) : RecyclerView.ViewHolder(item) {

  private val binding = LayoutTrackBinding.bind(item)
  fun bind(list: Track) = with(binding) {
    songTitle.text = list.trackName
    artistName.text = list.artistName
    trackTime.text = SimpleDateFormat(
      "mm:ss",
      Locale.getDefault()
    ).format(list.trackTimeMillis)


    Glide.with(itemView.context)
      .load(list.artworkUrl100)
      .placeholder(R.drawable.placeholder)
      .centerCrop()
      .transform(RoundedCorners(8))
      .into(trackCover)

  }
}