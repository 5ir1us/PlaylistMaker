package com.example.playlistmaker.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.domain.repository.SharedPreferencesProvider

class SharedPreferencesProviderImpl(private val sharedPreferences: SharedPreferences): SharedPreferencesProvider {

  override fun saveString(key: String, value: String) {
    sharedPreferences.edit().putString(key, value).apply()
  }

  override fun getString(key: String, defaultValue: String): String {
    return sharedPreferences.getString(key, defaultValue) ?: defaultValue
  }

  override fun saveBoolean(key: String, value: Boolean) {
    sharedPreferences.edit().putBoolean(key, value).apply()
  }

  override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
    return sharedPreferences.getBoolean(key, defaultValue)
  }
}