package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.SearchTrackRepository

class FavoritesViewModel(private val trackRepository: SearchTrackRepository) : ViewModel() {



  private val _favoriteTracks = MutableLiveData<List<Track>>()
  val favoriteTracks: LiveData<List<Track>> = _favoriteTracks


}