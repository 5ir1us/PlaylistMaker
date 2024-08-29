package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.TrackHistoryInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TrackHistoryRepository

class TrackHistoryInteractorImpl(
  private val repository: TrackHistoryRepository
) : TrackHistoryInteractor {
  override fun addTrackToHistory(track: Track) {
    val currentHistory = repository.getSearchTrackHistory().toMutableList()

    val existingTrackIndex = currentHistory.indexOfFirst { it.trackId == track.trackId }
    if (existingTrackIndex != -1) {
      currentHistory.removeAt(existingTrackIndex)
    }

    currentHistory.add(0, track)

    // Ограничим историю до 10 треков
    if (currentHistory.size > 10) {
      currentHistory.removeAt(currentHistory.size - 1)
    }
    repository.saveTrackHistory(currentHistory)
  }

  override fun getHistory(): List<Track> {
    return repository.getSearchTrackHistory().toList()
  }

  override fun clearHistory() {
    repository.clearTrackSearchHistory()
  }
}