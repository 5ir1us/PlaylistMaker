package com.example.playlistmaker.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track

class TrackAdapter(
  private val trackList: ArrayList<Track>,
) : RecyclerView.Adapter<TrackViewHolder>() {

  private var itemClickListener: ((Track) -> Unit)? = null

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
    val track = trackList[position]
    holder.bind(track)
    holder.itemView.setOnClickListener {

      itemClickListener?.invoke(track)
    }
  }

  override fun getItemCount(): Int = trackList.size

  @SuppressLint("NotifyDataSetChanged")
  fun updateTracks(newTracks: ArrayList<Track>) {
    trackList.clear()  // Очищаем
    trackList.addAll(newTracks)  // Добавляем
    notifyDataSetChanged()  // Обновляем адаптер todo new clear
  }

  fun setItemClickListener(listener: (Track) -> Unit) {
    itemClickListener = listener
  }
}


