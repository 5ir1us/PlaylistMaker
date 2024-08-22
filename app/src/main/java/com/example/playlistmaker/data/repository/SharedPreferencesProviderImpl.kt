package com.example.playlistmaker.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.domain.repository.SharedPreferencesProvider

class SharedPreferencesProviderImpl(private  val context: Context): SharedPreferencesProvider {
  override fun provideSharedPreferences(): SharedPreferences {
    return context.getSharedPreferences("your_prefs_name", Context.MODE_PRIVATE)
  }
}