package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.api.SearchTracksInteractor
 import com.example.playlistmaker.domain.api.SearchTrackRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchTracksInteractorImpl(
  private val repository: SearchTrackRepository
) : SearchTracksInteractor {



  override fun searchTracks(
    query: String,
    callback: (ArrayList<Track>) -> Unit,
  ) {
    GlobalScope.launch(Dispatchers.IO) { // Запуск корутины на фоновом потоке (IO)
      val tracks = repository.searchTracks(query) // Выполнение поиска треков
      withContext(Dispatchers.Main) { // Возвращаемся на главный поток для обновления UI
        callback(tracks) // Вызываем callback с результатами поиска
      }
    }
  }
}