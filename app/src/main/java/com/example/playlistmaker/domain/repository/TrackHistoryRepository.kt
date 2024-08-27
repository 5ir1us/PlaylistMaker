package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track

interface TrackHistoryRepository {
  fun saveTrackHistory(track: MutableList<Track>)
  fun getSearchTrackHistory(): Array<Track>
  fun clearTrackSearchHistory()
}