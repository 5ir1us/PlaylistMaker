package com.example.playlistmaker.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemBottomSheetPlaylistBinding
import com.example.playlistmaker.domain.model.PlaylistModel

class PlaylistBottomSheetAdapter (
    private var playlists: List<PlaylistModel>,
    private val onPlaylistClick: (PlaylistModel) -> Unit
) : RecyclerView.Adapter<PlaylistBottomSheetAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemBottomSheetPlaylistBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bottom_sheet_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        with(holder.binding) {
            playlistName.text = playlist.name
            playlistDescription.text = "${playlist.trackCount} tracks"

            root.setOnClickListener { onPlaylistClick(playlist) }
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun updateData(newPlaylists: List<PlaylistModel>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}