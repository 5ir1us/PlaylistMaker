package com.example.playlistmaker.domain.repository

interface ThemeRepository {
  fun saveThemeState(): Boolean
  fun getThemeState(enabled: Boolean)
}