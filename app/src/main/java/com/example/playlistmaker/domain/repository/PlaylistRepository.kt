package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.data.db.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(name: String, description: String?, coverPath: String?)
    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
    suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean

    suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

      suspend fun updatePlaylist(playlistId: Long, name: String, description: String?, coverPath: String?)

}