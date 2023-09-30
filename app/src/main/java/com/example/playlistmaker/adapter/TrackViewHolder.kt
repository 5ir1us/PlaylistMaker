package com.example.playlistmaker

import android.view.RoundedCorner
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.LayoutTrackBinding

class TrackViewHolder(item: View) : RecyclerView.ViewHolder(item) {


    val binding = LayoutTrackBinding.bind(item)
    fun bind(list: Track) = with(binding) {
        songTitle.text = list.trackName
        artistName.text = list.artistName
        trackTime.text = list.trackTime
        Glide.with(itemView.context)
            .load(list.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(trackCover)


    }
}