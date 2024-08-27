package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Track

interface SearchTracksInteractor {
  fun searchTracks(query: String, callback: (ArrayList<Track>) -> Unit)
}