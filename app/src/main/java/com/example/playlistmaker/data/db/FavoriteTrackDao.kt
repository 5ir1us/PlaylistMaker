package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrack): Long

    @Delete
    suspend fun deleteTrack(track: FavoriteTrack): Int

    @Query("SELECT * FROM favorite_tracks ORDER BY timeAdd DESC")
    fun getAllFavoriteTracks(): Flow<List<FavoriteTrack>>

    @Query("SELECT trackId FROM favorite_tracks")
    fun getAllFavoriteTrackIds(): Flow<List<Int>>
}

