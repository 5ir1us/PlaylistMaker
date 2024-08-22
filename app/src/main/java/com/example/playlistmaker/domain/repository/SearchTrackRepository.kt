package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track

interface SearchTrackRepository {
  fun searchTracks(query: String): ArrayList<Track>
  // fun saveTrackToHistory(track: Track)
  // fun getTrackHistory(): ArrayList<Track>
}