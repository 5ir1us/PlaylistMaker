package com.example.playlistmaker.presentation.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.domain.model.PlaylistModel
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
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

            // Используем `plurals` для отображения правильного окончания
            val context = holder.itemView.context
            val trackCountText = context.resources.getQuantityString(
                R.plurals.track_count,
                playlist.trackCount,
                playlist.trackCount
            )
            playlistDescription.text = trackCountText




            if (!playlist.coverPath.isNullOrEmpty() && File(playlist.coverPath).exists()) {
                Glide.with(playlistCover.context)
                    .load(File(playlist.coverPath))
                    .transform(CenterCrop(), RoundedCorners(8))
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

