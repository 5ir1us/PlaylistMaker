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

        val isTrackInPlaylist = trackInPlaylistDao.isTrackInPlaylist(track.trackId, playlist.id)

        if (!isTrackInPlaylist) {

            val trackInPlaylist = TrackConverter.convertToTrackInPlaylist(track, playlist.id)
            trackInPlaylistDao.insertTrack(trackInPlaylist)

            val updatedTrackIds = if (playlist.trackIds == "[]") {
                "[${track.trackId}]"
            } else {
                playlist.trackIds.dropLast(1) + ",${track.trackId}]"
            }

            val updatedPlaylist = playlist.copy(
                trackIds = updatedTrackIds,
                trackCount = playlist.trackCount + 1
            )

            playlistDao.updatePlaylist(updatedPlaylist)
        }
    }

    override suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean {
        return trackInPlaylistDao.isTrackInPlaylist(trackId, playlistId)
    }

    override suspend fun removeTrackFromPlaylist(track: Track, playlist: Playlist) {
         trackInPlaylistDao.deleteTrack(track.trackId, playlist.id)

         val updatedTrackIds = playlist.trackIds.removeSurrounding("[", "]")
            .split(",")
            .filter { it.toIntOrNull() != track.trackId }
            .joinToString(",", "[", "]")

        val updatedPlaylist = playlist.copy(
            trackIds = updatedTrackIds,
            trackCount = updatedTrackIds.removeSurrounding("[", "]").split(",").size
        )

         playlistDao.updatePlaylist(updatedPlaylist)
    }


    override suspend fun deletePlaylist(playlist: Playlist) {
        try {
            // Удаление треков, связанных с плейлистом
            trackInPlaylistDao.deleteTracksByPlaylistId(playlist.id)

            // Удаление самого плейлиста
            playlistDao.deletePlaylist(playlist)
        } catch (e: Exception) {
            Log.e("PlaylistRepositoryImpl", "Error deleting playlist", e)
            throw e
        }
    }


    override suspend fun updatePlaylist(playlistId: Long, name: String, description: String?, coverPath: String?) {
        val playlist = playlistDao.getPlaylistById(playlistId)
        val updatedPlaylist = playlist.copy(
            name = name,
            description = description ?: playlist.description,
            coverPath = coverPath ?: playlist.coverPath
        )
        playlistDao.updatePlaylist(updatedPlaylist)
    }

}
