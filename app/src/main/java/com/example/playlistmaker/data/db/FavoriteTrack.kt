package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class FavoriteTrack(
    @PrimaryKey val trackId: Int,
    val artworkUrl: String,
    val trackName: String,
    val artistName: String,
    val albumName: String?,
    val releaseYear: Int,
    val genre: String,
    val country: String,
    val duration: String,
    val audioUrl: String,
    val inFavorite: Boolean,
    val timeAdd: Long
)