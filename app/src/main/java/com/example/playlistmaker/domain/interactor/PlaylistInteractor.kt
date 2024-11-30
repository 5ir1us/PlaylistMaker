package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.data.db.Playlist
import com.example.playlistmaker.domain.model.PlaylistModel
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(name: String, description: String?, coverPath: String?)
    fun getAllPlaylists(): Flow<List<PlaylistModel>>

    suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean
    suspend fun addTrackToPlaylist(track: Track, playlist: PlaylistModel)

    suspend fun removeTrackFromPlaylist(track: Track, playlist: PlaylistModel)

    suspend fun deletePlaylist(playlist: PlaylistModel)


    suspend fun updatePlaylist(playlistId: Long, name: String, description: String?, coverPath: String?)
}