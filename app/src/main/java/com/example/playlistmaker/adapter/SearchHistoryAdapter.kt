package com.example.playlistmaker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Track

class SearchHistoryAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    val historyList  = mutableListOf<Track>()

  fun updateData(newData: MutableList<Track>) {
    historyList.clear()
    historyList.addAll(newData)
    notifyDataSetChanged()
  }
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): TrackViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_track, parent, false)
    return TrackViewHolder(view)
  }

  override fun getItemCount(): Int = historyList.size

  override fun onBindViewHolder(
    holder: TrackViewHolder,
    position: Int,
  ) {
    holder.bind(historyList[position])
  }

  fun clearTracksHistory(newData: MutableList<Track>) {
    newData.clear()
    notifyDataSetChanged()
  }
}

