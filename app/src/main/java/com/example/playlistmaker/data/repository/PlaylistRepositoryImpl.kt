package com.example.playlistmaker.data.repository

import android.util.Log
import com.example.playlistmaker.data.TrackConverter
import com.example.playlistmaker.data.db.Playlist
import com.example.playlistmaker.data.db.PlaylistDao
import com.example.playlistmaker.data.db.TrackInPlaylistDao
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val trackInPlaylistDao: TrackInPlaylistDao
) : PlaylistRepository {

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

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        Log.d("PlaylistRepository", "Checking if track exists in playlist...")

        // Проверка на наличие трека в плейлисте
        val isTrackInPlaylist = trackInPlaylistDao.isTrackInPlaylist(track.trackId, playlist.id)
        Log.d("PlaylistRepository", "isTrackInPlaylist result: $isTrackInPlaylist")

        if (!isTrackInPlaylist) {
            Log.d("PlaylistRepository", "Track not found in playlist, adding now...")

            // Преобразование Track в TrackInPlaylist
            val trackInPlaylist = TrackConverter.convertToTrackInPlaylist(track, playlist.id)

            // Добавление трека в базу данных
            trackInPlaylistDao.insertTrack(trackInPlaylist)
            Log.d("PlaylistRepository", "Track added to tracks_in_playlists table")

            // Обновление плейлиста с добавлением трека
            val updatedTrackIds = if (playlist.trackIds == "[]") {
                "[${track.trackId}]"
            } else {
                playlist.trackIds.dropLast(1) + ",${track.trackId}]"
            }

            val updatedPlaylist = playlist.copy(
                trackIds = updatedTrackIds,
                trackCount = playlist.trackCount + 1
            )

            // Обновляем плейлист
            playlistDao.updatePlaylist(updatedPlaylist)
            Log.d("PlaylistRepository", "Playlist updated with new track count: ${updatedPlaylist.trackCount}")
        } else {
            Log.d("PlaylistRepository", "Track already exists in playlist, skipping addition.")
        }
    }

    override suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean {
        return trackInPlaylistDao.isTrackInPlaylist(trackId, playlistId)
    }
}