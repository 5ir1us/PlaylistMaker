package com.example.playlistmaker.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.presentationModule

import com.example.playlistmaker.domain.interactor.ThemeInteractor
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

  override fun onCreate() {
    super.onCreate()

    val appModules = listOf(dataModule, domainModule, presentationModule)
    startKoin {
      androidContext(this@App)
      modules(appModules)
    }
    val themeInteractor: ThemeInteractor = get()
    applySavedTheme(themeInteractor)
  }

  private fun applySavedTheme(themeInteractor: ThemeInteractor) {
    val isDarkThemeEnabled = themeInteractor.isDarkThemeEnabled()
    AppCompatDelegate.setDefaultNightMode(
      if (isDarkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
    )
  }
}