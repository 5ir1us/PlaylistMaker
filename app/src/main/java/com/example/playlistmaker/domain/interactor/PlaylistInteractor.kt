package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.data.db.Playlist
import com.example.playlistmaker.domain.model.PlaylistModel
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(name: String, description: String?, coverPath: String?)
    fun getAllPlaylists(): Flow<List<PlaylistModel>>
}