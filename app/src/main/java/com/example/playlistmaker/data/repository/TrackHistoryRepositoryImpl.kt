package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.Constants
import com.example.playlistmaker.domain.repository.SharedPreferencesProvider
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.google.gson.Gson

class TrackHistoryRepositoryImpl(private val preferencesRepository: SharedPreferencesProvider) :
  TrackHistoryRepository {

  override fun clearTrackSearchHistory() {
    preferencesRepository.saveString(Constants.SEARCH_HISTORY_KEY, "")
  }

  override fun getSearchTrackHistory(): Array<Track> {
    val json = preferencesRepository.getString(Constants.SEARCH_HISTORY_KEY, "")
    return Gson().fromJson(json, Array<Track>::class.java) ?: emptyArray()
  }

  override fun saveTrackHistory(track: MutableList<Track>) {
    val json = Gson().toJson(track)
    preferencesRepository.saveString(Constants.SEARCH_HISTORY_KEY, json)
  }
}


