package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TrackHistoryRepository

class TrackHistoryInteractor(
  private val repository: TrackHistoryRepository
) {
  fun addTrackToHistory(track: Track) {
    val currentHistory = repository.getSearchTrackHistory().toMutableList()
    currentHistory.add(0, track)
    // Ограничим историю до 10 треков
    if (currentHistory.size > 10) {
      currentHistory.removeAt(currentHistory.size - 1)
    }
    repository.saveTrackHistory(currentHistory)
  }

  fun getHistory(): List<Track> {
    return repository.getSearchTrackHistory().toList()
  }

  fun clearHistory() {
    repository.clearTrackSearchHistory()
  }
}