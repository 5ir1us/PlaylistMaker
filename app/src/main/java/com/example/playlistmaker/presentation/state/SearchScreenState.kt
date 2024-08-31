package com.example.playlistmaker.presentation.state

import com.example.playlistmaker.domain.model.Track

data class SearchScreenState(
  val searchResults: List<Track> = emptyList(),
  val history: List<Track> = emptyList(),
  val isHistoryVisible: Boolean = true,
  val isLoading: Boolean = false,
  val isClearIconVisible: Boolean = false,
  val showNoResults: Boolean = false,
  val showError: Boolean = false
)
