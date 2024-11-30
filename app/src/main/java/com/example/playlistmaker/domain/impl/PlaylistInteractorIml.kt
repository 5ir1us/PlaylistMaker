package com.example.playlistmaker.domain.impl

import android.util.Log
import com.example.playlistmaker.data.TrackConverter
import com.example.playlistmaker.data.TrackConverter.fromEntity
import com.example.playlistmaker.domain.repository.PlaylistRepository
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.model.PlaylistModel
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorIml(private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun createPlaylist(name: String, description: String?, coverPath: String?) {
        repository.createPlaylist(name, description, coverPath)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistModel>> {
        return repository.getAllPlaylists().map { playlists ->
            playlists.map { fromEntity(it) }
        }
    }


    override suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean {
        return repository.isTrackInPlaylist(trackId, playlistId)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: PlaylistModel) {
        repository.addTrackToPlaylist(track, TrackConverter.toEntity(playlist))
    }

    override suspend fun removeTrackFromPlaylist(track: Track, playlist: PlaylistModel) {
        repository.removeTrackFromPlaylist(track, TrackConverter.toEntity(playlist))
    }

    override suspend fun deletePlaylist(playlist: PlaylistModel) {
        try {
            // Удаление треков из плейлиста
            repository.deletePlaylist(TrackConverter.toEntity(playlist))
            Log.d("PlaylistInteractor", "Playlist ${playlist.name} successfully deleted")
        } catch (e: Exception) {
            Log.e("PlaylistInteractor", "Error deleting playlist", e)
            throw e
        }
    }


    override suspend fun updatePlaylist(playlistId: Long, name: String, description: String?, coverPath: String?) {
        repository.updatePlaylist(playlistId, name, description, coverPath)
    }

}