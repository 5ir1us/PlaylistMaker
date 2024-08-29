package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.SearchTracksInteractor
import com.example.playlistmaker.domain.interactor.TrackHistoryInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.state.SearchScreenState
import kotlinx.coroutines.launch

class SearchViewModel(
  private val searchTracksInteractor: SearchTracksInteractor,
  private val trackHistoryInteractor: TrackHistoryInteractor,
) : ViewModel() {

  private val _screenState = MutableLiveData<SearchScreenState>()
  val searchScreenState: LiveData<SearchScreenState> get() = _screenState

  // private var debouncePeriod = 3000L // Период дебаунса

  init {
    _screenState.value = SearchScreenState(
      history = trackHistoryInteractor.getHistory(),
      isHistoryVisible = false // История скрыта по умолчанию
    )
  }

  fun onQueryTextChanged(query: String) {
    _screenState.value = _screenState.value?.copy(
      isClearIconVisible = query.isNotEmpty(),
      isHistoryVisible = query.isEmpty() && _screenState.value?.history?.isNotEmpty() == true
    )
  }

  fun searchTracks(query: String) {
    if (query.isNotEmpty()) {
      _screenState.value = _screenState.value?.copy(
        isLoading = true,
        isHistoryVisible = false,
        showNoResults = false,
        showError = false,
        searchResults = emptyList()
      )

      viewModelScope.launch {
        try {
          val tracks = searchTracksInteractor.searchTracks(query)
          if (tracks.isEmpty()) {
            _screenState.value = _screenState.value?.copy(
              isLoading = false,
              showNoResults = true,
              searchResults = tracks,
              isHistoryVisible = false,
              showError = false
            )
          } else {
            _screenState.value = _screenState.value?.copy(
              isLoading = false,
              searchResults = tracks,
              isHistoryVisible = false,
              showNoResults = false,
              showError = false
            )
          }
        } catch (e: Exception) {
          _screenState.value = _screenState.value?.copy(
            isLoading = false,
            showError = true
          )
        }
      }
    } else {
      _screenState.value = _screenState.value?.copy(
        searchResults = emptyList(),
        isHistoryVisible = trackHistoryInteractor.getHistory().isNotEmpty(),
        isLoading = false,
        showNoResults = false,
        showError = false
      )
    }
  }

  fun resetStateToHistory() {
    _screenState.value = _screenState.value?.copy(
      searchResults = emptyList(),
      isHistoryVisible = trackHistoryInteractor.getHistory().isNotEmpty(),
      isLoading = false,
      showNoResults = false,
      showError = false
    )
  }

  fun clearSearchHistory() {
    trackHistoryInteractor.clearHistory()
    _screenState.value = _screenState.value?.copy(
      history = emptyList(),
      isHistoryVisible = false
    )
  }

  fun addTrackToHistory(track: Track) {
    trackHistoryInteractor.addTrackToHistory(track)
    _screenState.value = _screenState.value?.copy(
      history = trackHistoryInteractor.getHistory(),
      isHistoryVisible = false
    )
  }

  fun onSearchFocusChanged(hasFocus: Boolean) {
    _screenState.value = _screenState.value?.copy(
      isHistoryVisible = hasFocus && _screenState.value?.history?.isNotEmpty() == true
    )
  }
}
