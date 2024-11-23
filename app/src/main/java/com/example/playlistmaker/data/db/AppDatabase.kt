package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [FavoriteTrack::class, Playlist::class, TrackInPlaylist::class], version = 4)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteTrackDao(): FavoriteTrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackInPlaylistDao(): TrackInPlaylistDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `tracks_in_playlists` (
                        `trackId` INTEGER NOT NULL,
                        `playlistId` INTEGER NOT NULL,
                        `artworkUrl` TEXT,
                        `trackName` TEXT NOT NULL,
                        `artistName` TEXT NOT NULL,
                        `albumName` TEXT,
                        `releaseYear` INTEGER,
                        `genre` TEXT,
                        `country` TEXT,
                        `duration` TEXT,
                        `audioUrl` TEXT NOT NULL,
                        `isFavorite` INTEGER NOT NULL,
                        `timeAdded` INTEGER NOT NULL,
                        PRIMARY KEY(`trackId`, `playlistId`)
                    )
                """.trimIndent()
                )
            }
        }


    }
}