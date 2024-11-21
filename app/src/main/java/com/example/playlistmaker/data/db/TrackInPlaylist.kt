package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_playlists")
data class TrackInPlaylist(
    @PrimaryKey val trackId: Int,
    val trackName: String,
    val artistName: String,
    val albumName: String?,
    val releaseYear: Int?,
    val genre: String?,
    val country: String?,
    val duration: String?,
    val audioUrl: String,
    val isFavorite: Boolean,
    val timeAdded: Long
)
