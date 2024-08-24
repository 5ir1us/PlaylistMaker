package com.example.playlistmaker.domain.repository

interface SharedPreferencesProvider {
  // fun provideSharedPreferences(): SharedPreferences

  fun saveString(
    key: String,
    value: String,
  )

  fun getString(
    key: String,
    defaultValue: String,
  ): String

  fun saveBoolean(
    key: String,
    value: Boolean,
  )

  fun getBoolean(
    key: String,
    defaultValue: Boolean,
  ): Boolean
}