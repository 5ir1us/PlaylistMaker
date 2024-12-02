package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface TrackInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylist): Long

    @Query("SELECT COUNT(*) > 0 FROM tracks_in_playlists WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean


    @Query("SELECT * FROM tracks_in_playlists")
    fun getAllTracks(): Flow<List<TrackInPlaylist>>

    @Query("SELECT * FROM tracks_in_playlists WHERE playlistId = :playlistId")
    fun getTracksInPlaylist(playlistId: Long): Flow<List<TrackInPlaylist>>

    @Query("DELETE FROM tracks_in_playlists WHERE trackId = :trackId AND playlistId = :playlistId")
    suspend fun deleteTrack(trackId: Int, playlistId: Long): Int

    @Query("DELETE FROM tracks_in_playlists WHERE playlistId = :playlistId")
    suspend fun deleteTracksByPlaylistId(playlistId: Long)


}

