package com.example.playlistmaker.domain.repository

interface ThemeRepository {
  fun saveThemeState(enabled: Boolean)
  fun getThemeState(): Boolean
}