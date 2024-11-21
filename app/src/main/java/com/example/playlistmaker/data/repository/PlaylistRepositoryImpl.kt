package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.db.Playlist
import com.example.playlistmaker.data.db.PlaylistDao
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl (private val playlistDao: PlaylistDao) : PlaylistRepository {

    override suspend fun createPlaylist(name: String, description: String?, coverPath: String?) {
        val playlist = Playlist(
            name = name,
            description = description,
            coverPath = coverPath
        )
        playlistDao.insertPlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists()
    }
}