package com.example.playlistmaker.data.datasource

import android.content.SharedPreferences

class SharedPreferencesDataSource (private val sharedPreferences: SharedPreferences)
{

  fun saveString(key: String, value: String) {
    sharedPreferences.edit().putString(key, value).apply()
  }

  fun getString(key: String, defaultValue: String): String {
    return sharedPreferences.getString(key, defaultValue) ?: defaultValue
  }

  fun saveBoolean(key: String, value: Boolean) {
    sharedPreferences.edit().putBoolean(key, value).apply()
  }

  fun getBoolean(key: String, defaultValue: Boolean): Boolean {
    return sharedPreferences.getBoolean(key, defaultValue)
  }
}