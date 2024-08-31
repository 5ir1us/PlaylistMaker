package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.SearchTracksInteractor
import com.example.playlistmaker.domain.repository.SearchTrackRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchTracksInteractorImpl(
  private val repository: SearchTrackRepository
) : SearchTracksInteractor {

  override suspend fun searchTracks(query: String): ArrayList<Track> {
    return withContext(Dispatchers.IO) {
      repository.searchTracks(query) // Выполнение поиска треков в фоновом потоке
    }
  }
}