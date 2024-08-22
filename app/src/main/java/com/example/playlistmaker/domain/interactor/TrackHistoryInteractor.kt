package com.example.playlistmaker.domain.interactor

import com.example.playlistmaker.domain.model.Track

interface TrackHistoryInteractor {

  fun addTrackToHistory (track: Track)
  fun getHistory () :List <Track>
  fun clearHistory ()
}