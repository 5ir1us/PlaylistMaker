package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.datasource.SharedPreferencesDataSource
import com.example.playlistmaker.domain.repository.ThemeRepository

class ThemeRepositoryImpl(private val preferencesDataSource: SharedPreferencesDataSource) : ThemeRepository {

  companion object {
    private const val DARK_THEME_KEY = "dark_theme_key"
  }

  override fun saveThemeState(): Boolean {
    return preferencesDataSource.getBoolean(DARK_THEME_KEY, false)
  }

  override fun getThemeState(enabled: Boolean) {
    preferencesDataSource.saveBoolean(DARK_THEME_KEY, enabled)  }
}