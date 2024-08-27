package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.interactor.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.interactor.ThemeInteractor

class SettingsViewModelFactory(
  private val themeInteractor: ThemeInteractor,
  private val externalNavigatorInteractor: ExternalNavigatorInteractor,
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return SettingsViewModel(themeInteractor, externalNavigatorInteractor) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}