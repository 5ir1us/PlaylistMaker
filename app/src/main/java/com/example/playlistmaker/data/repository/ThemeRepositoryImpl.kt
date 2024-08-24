package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.repository.SharedPreferencesProvider
import com.example.playlistmaker.domain.repository.ThemeRepository

class ThemeRepositoryImpl(private val preferencesRepository: SharedPreferencesProvider) : ThemeRepository {

  companion object {
    private const val DARK_THEME_KEY = "dark_theme_key"
  }

  override fun saveThemeState(): Boolean {
    return preferencesRepository.getBoolean(DARK_THEME_KEY, false)
  }

  override fun getThemeState(enabled: Boolean) {
    preferencesRepository.saveBoolean(DARK_THEME_KEY, enabled)  }
}