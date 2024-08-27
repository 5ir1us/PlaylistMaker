package com.example.playlistmaker.presentation

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.Constants

class App : Application( ) {

  private lateinit var   sharedPreferences: SharedPreferences
  private var darkTheme = false
  override fun onCreate() {
    super.onCreate()
    sharedPreferences = getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
    switchTheme(loadThemeMode())
  }


  fun switchTheme(darkThemeEnabled: Boolean) {
    darkTheme = darkThemeEnabled
    AppCompatDelegate.setDefaultNightMode(
      if (darkThemeEnabled) {
        AppCompatDelegate.MODE_NIGHT_YES
      } else {
        AppCompatDelegate.MODE_NIGHT_NO
      }
    )
  }

  fun saveThemeMode(isDarkMode: Boolean) {
    sharedPreferences.edit().putBoolean(Constants.THEME_MODE_KEY, isDarkMode).apply()

  }

    fun loadThemeMode(): Boolean {
    return sharedPreferences.getBoolean(Constants.THEME_MODE_KEY, false)
  }
}