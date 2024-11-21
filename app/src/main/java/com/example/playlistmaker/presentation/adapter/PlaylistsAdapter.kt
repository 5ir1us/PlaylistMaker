package com.example.playlistmaker.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.domain.model.PlaylistModel
import java.io.File

class PlaylistsAdapter(private var playlists: List<PlaylistModel>) :
    RecyclerView.Adapter<PlaylistsAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemPlaylistBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        with(holder.binding) {
            playlistName.text = playlist.name
            playlistDescription.text = "${playlist.trackCount} tracks"

            if (!playlist.coverPath.isNullOrEmpty() && File(playlist.coverPath).exists()) {
                Glide.with(playlistCover.context)
                    .load(File(playlist.coverPath))
                    .placeholder(R.drawable.placeholder)
                    .into(playlistCover)
            } else {
                playlistCover.setImageResource(R.drawable.placeholder)
            }
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun updateData(newPlaylists: List<PlaylistModel>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }


    //удол мусор
    private fun deleteOldCover(path: String?) {
        path?.let {
            val file = File(it)
            if (file.exists()) {
                file.delete()
            }
        }
    }
}

