package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track

interface SearchTracksInteractor {
  fun searchTracks(query: String, callback: (ArrayList<Track>) -> Unit)
}