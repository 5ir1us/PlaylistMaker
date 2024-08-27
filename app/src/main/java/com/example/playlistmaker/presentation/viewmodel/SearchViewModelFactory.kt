package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.interactor.SearchTracksInteractor
import com.example.playlistmaker.domain.interactor.TrackHistoryInteractor

class SearchViewModelFactory(
  private val searchTracksInteractor: SearchTracksInteractor,
  private val trackHistoryInteractor: TrackHistoryInteractor
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
      return SearchViewModel(searchTracksInteractor, trackHistoryInteractor) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}