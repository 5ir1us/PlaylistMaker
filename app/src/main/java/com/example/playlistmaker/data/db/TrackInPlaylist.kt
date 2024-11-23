package com.example.playlistmaker.data.db

import androidx.room.Entity

@Entity(tableName = "tracks_in_playlists", primaryKeys = ["trackId", "playlistId"])
data class TrackInPlaylist(
    val trackId: Int,
    val playlistId: Long,
    val artworkUrl: String,
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
