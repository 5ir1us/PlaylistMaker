package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.SearchTracksInteractor
import com.example.playlistmaker.domain.repository.SearchTrackRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchTracksInteractorImpl(
  private val repository: SearchTrackRepository
) : SearchTracksInteractor {

  override fun searchTracks(query: String): Flow<List<Track>> {
    return repository.searchTracks(query)  // Возвращаем Flow от репозитория
  }
}
