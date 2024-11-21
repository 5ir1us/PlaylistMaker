package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.TrackConverter.fromEntity
import com.example.playlistmaker.domain.repository.PlaylistRepository
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.model.PlaylistModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorIml (private val repository: PlaylistRepository):PlaylistInteractor {

   override suspend fun createPlaylist(name: String, description: String?, coverPath: String?) {
        repository.createPlaylist(name, description, coverPath)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistModel>> {
        return repository.getAllPlaylists().map { playlists ->
            playlists.map { fromEntity(it) }
        }
    }
}