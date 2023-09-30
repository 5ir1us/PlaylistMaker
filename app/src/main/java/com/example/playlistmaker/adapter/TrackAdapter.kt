package com.example.playlistmaker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Track

class TrackAdapter(private val trackList: ArrayList<Track>) :
  RecyclerView.Adapter<TrackViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): TrackViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_track, parent, false)
    return TrackViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: TrackViewHolder,
    position: Int,
  ) {
    holder.bind(trackList[position])
  }

  override fun getItemCount(): Int = trackList.size

  @SuppressLint("NotifyDataSetChanged")
  fun updateTracks(newTracks: ArrayList<Track>) {
    trackList.clear()
    notifyDataSetChanged()
  }
}


