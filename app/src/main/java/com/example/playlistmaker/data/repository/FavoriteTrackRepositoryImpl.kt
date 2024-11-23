package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.TrackConverter
import com.example.playlistmaker.data.db.FavoriteTrackDao
import com.example.playlistmaker.domain.model.Track

import com.example.playlistmaker.domain.repository.FavoriteTrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTrackRepositoryImpl(
    private val favoriteTrackDao: FavoriteTrackDao
) : FavoriteTrackRepository {

    override suspend fun addTrackToFavorites(track: Track) {
        favoriteTrackDao.insertTrack(
            TrackConverter.convertToFavoriteEntity(
                TrackConverter.convertToDto(
                    track
                )
            )
        )
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        favoriteTrackDao.deleteTrack(
            TrackConverter.convertToFavoriteEntity(
                TrackConverter.convertToDto(
                    track
                )
            )
        )
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> {
        return favoriteTrackDao.getAllFavoriteTracks()
            .map { favoriteTracks -> favoriteTracks.map { TrackConverter.convertToDomainModel(it) } }
    }

    override fun getAllFavoriteTrackIds(): Flow<List<Int>> { // Реализация нового метода
        return favoriteTrackDao.getAllFavoriteTrackIds()
    }
}