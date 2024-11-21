package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.data.db.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(name: String, description: String?, coverPath: String?)
    fun getAllPlaylists(): Flow<List<Playlist>>
}