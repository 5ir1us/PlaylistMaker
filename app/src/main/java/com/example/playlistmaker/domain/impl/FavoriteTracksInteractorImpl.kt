package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.FavoriteTracksInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.FavoriteTrackRepository
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val repository: FavoriteTrackRepository
) : FavoriteTracksInteractor {

    override suspend fun addTrackToFavorites(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        repository.removeTrackFromFavorites(track)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> {
        return repository.getAllFavoriteTracks()
    }
    override fun getAllFavoriteTrackIds(): Flow<List<Int>> { // Реализация нового метода
        return repository.getAllFavoriteTrackIds()
    }
}
