package com.example.playlistmaker.utils

import android.content.SharedPreferences
import com.example.playlistmaker.data.Track
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {

  fun clearSharedPreferencesHistory() {
    sharedPreferences.edit().clear().apply()

  }

  fun loadTrackFromSharedPreferences(): Array<Track> {
    val json = sharedPreferences.getString(Constants.SEARCH_HISTORY_KEY, null)// или[]
    return Gson().fromJson(json, Array<Track>::class.java) ?: emptyArray()
  }

  fun saveTrackToSharedPreferences(track: MutableList<Track>) {
    val json = Gson().toJson(track)
    sharedPreferences.edit().putString(Constants.SEARCH_HISTORY_KEY, json).apply()
  }



}


