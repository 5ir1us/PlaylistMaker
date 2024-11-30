package com.example.playlistmaker.presentation.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
        holder.bind(playlists[position], holder.itemView.context)
    }

    override fun getItemCount(): Int = playlists.size

    fun updatePlaylists(newPlaylists: List<PlaylistModel>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }


    inner class PlaylistViewHolder(private val binding: ItemBottomSheetPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: PlaylistModel, context: Context) {
            binding.playlistNameBottomSheet.text = playlist.name

            val trackCountText = context.resources.getQuantityString(
                R.plurals.track_count,
                playlist.trackCount,
                playlist.trackCount
            )
            binding.playlistTrackCount.text = trackCountText


            if (!playlist.coverPath.isNullOrEmpty() && File(playlist.coverPath).exists()) {
                Glide.with(binding.playlistCoverBottomSheet.context)
                    .load(File(playlist.coverPath))
                    .transform(CenterCrop(), RoundedCorners(dpToPx(2)))
                    .placeholder(R.drawable.placeholder)
                    .into(binding.playlistCoverBottomSheet)
            } else {
                binding.playlistCoverBottomSheet.setImageResource(R.drawable.placeholder)
            }


            binding.root.setOnClickListener { onPlaylistClicked(playlist) }
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

}
