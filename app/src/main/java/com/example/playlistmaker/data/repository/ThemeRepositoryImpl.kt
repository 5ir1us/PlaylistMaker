package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.datasource.SharedPreferencesDataSource
import com.example.playlistmaker.domain.repository.ThemeRepository

class ThemeRepositoryImpl(private val preferencesDataSource: SharedPreferencesDataSource) : ThemeRepository {

  companion object {
    private const val DARK_THEME_KEY = "dark_theme_key"
  }

  override fun saveThemeState(enabled: Boolean) {
    preferencesDataSource.saveBoolean(DARK_THEME_KEY, enabled)

  }

  override fun getThemeState() : Boolean{
    return preferencesDataSource.getBoolean(DARK_THEME_KEY, false) }
}