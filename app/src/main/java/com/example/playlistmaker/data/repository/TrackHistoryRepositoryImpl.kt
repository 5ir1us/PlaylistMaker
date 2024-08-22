package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.Constants
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.google.gson.Gson

class TrackHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) : TrackHistoryRepository {

  override fun clearTrackSearchHistory() {
    sharedPreferences.edit().clear().apply()
  }

  override fun getSearchTrackHistory(): Array<Track> {
    val json = sharedPreferences.getString(Constants.SEARCH_HISTORY_KEY, null)// или[]
    return Gson().fromJson(json, Array<Track>::class.java) ?: emptyArray()
  }

  override fun saveTrackHistory(track: MutableList<Track>) {
    val json = Gson().toJson(track)
    sharedPreferences.edit().putString(Constants.SEARCH_HISTORY_KEY, json).apply()
  }
}


