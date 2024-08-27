package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.interactor.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.interactor.ThemeInteractor

class SettingsViewModel(
  private val themeInteractor: ThemeInteractor,
  private val externalNavigatorInteractor: ExternalNavigatorInteractor,
) : ViewModel() {

  private val _isDarkThemeEnabled = MutableLiveData<Boolean>()
  val isDarkThemeEnabled: LiveData<Boolean> get() = _isDarkThemeEnabled

  init {
    _isDarkThemeEnabled.value = themeInteractor.isDarkThemeEnabled()
  }

  fun onThemeSwitchToggled(isChecked: Boolean) {
    if (isChecked) {
      themeInteractor.setDarkThemeEnabled(true)
    } else {
      themeInteractor.setDarkThemeEnabled(false)
    }
    _isDarkThemeEnabled.value = isChecked
  }

  fun shareApp() {
    externalNavigatorInteractor.shareApp()
  }

  fun openSupport() {
    externalNavigatorInteractor.openSupport()
  }

  fun openTerms() {
    externalNavigatorInteractor.openTerms()
  }
}