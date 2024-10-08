package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.ThemeInteractor
import com.example.playlistmaker.domain.repository.ThemeRepository

class  ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {

  override fun isDarkThemeEnabled(): Boolean {
    return repository.getThemeState()
  }

  override fun setDarkThemeEnabled(enabled: Boolean) {
    repository.saveThemeState(enabled)
  }
}