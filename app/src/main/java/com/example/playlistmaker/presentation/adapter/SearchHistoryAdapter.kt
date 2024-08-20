package com.example.playlistmaker.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track

class SearchHistoryAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

  val historyList = mutableListOf<Track>()

  private var itemClickListener: ((Track) -> Unit)? = null


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
    val historyTrack = historyList[position]
    holder.bind(historyTrack)

    holder.itemView.setOnClickListener {
      itemClickListener?.invoke(historyTrack)
    }

  }

  fun clearTracksHistory(newData: MutableList<Track>) {
    newData.clear()
    notifyDataSetChanged()
  }

  fun setItemClickListener(listener: (Track) -> Unit) {
    itemClickListener = listener
  }
}

