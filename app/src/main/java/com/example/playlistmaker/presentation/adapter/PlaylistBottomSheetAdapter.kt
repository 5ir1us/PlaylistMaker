package com.example.playlistmaker.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemBottomSheetPlaylistBinding
import com.example.playlistmaker.domain.model.PlaylistModel
import java.io.File

class PlaylistBottomSheetAdapter(
    private var playlists: List<PlaylistModel>,
    private val onPlaylistClicked: (PlaylistModel) -> Unit
) : RecyclerView.Adapter<PlaylistBottomSheetAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemBottomSheetPlaylistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

    fun updatePlaylists(newPlaylists: List<PlaylistModel>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }

    inner class PlaylistViewHolder(private val binding: ItemBottomSheetPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: PlaylistModel) {
            binding.playlistNameBottomSheet.text = playlist.name
//            binding.playlistDescriptionBottomSheet.text = playlist.description ?: ""
            binding.playlistTrackCount.text = "${playlist.trackCount} tracks"


            if (!playlist.coverPath.isNullOrEmpty() && File(playlist.coverPath).exists()) {
                Glide.with(binding.playlistCoverBottomSheet.context)
                    .load(File(playlist.coverPath))
                    .placeholder(R.drawable.placeholder)
                    .into(binding.playlistCoverBottomSheet)
            } else {
                binding.playlistCoverBottomSheet.setImageResource(R.drawable.placeholder)
            }


            binding.root.setOnClickListener { onPlaylistClicked(playlist) }
        }
    }
}
