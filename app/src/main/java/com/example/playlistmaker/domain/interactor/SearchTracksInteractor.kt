package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Track

interface SearchTracksInteractor {
suspend  fun searchTracks(query: String ): ArrayList<Track>
}