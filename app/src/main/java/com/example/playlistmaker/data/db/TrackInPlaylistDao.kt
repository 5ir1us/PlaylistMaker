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

    @Query("SELECT * FROM tracks_in_playlists WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Int): TrackInPlaylist?

    @Query("SELECT * FROM tracks_in_playlists")
    fun getAllTracks(): Flow<List<TrackInPlaylist>>

    @Query("DELETE FROM tracks_in_playlists WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Int): Int
}