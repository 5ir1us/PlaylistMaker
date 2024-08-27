package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractor

class AudioPlayerViewModelFactory(private val audioPlayerInteractor: AudioPlayerInteractor) :
  ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(AudioPlayerViewModel::class.java)) {
      return AudioPlayerViewModel(audioPlayerInteractor) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}