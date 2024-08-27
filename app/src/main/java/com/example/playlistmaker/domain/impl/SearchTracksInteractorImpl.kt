package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.SearchTracksInteractor
import com.example.playlistmaker.domain.repository.SearchTrackRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchTracksInteractorImpl(
  private val scope: CoroutineScope,
  private val repository: SearchTrackRepository,
) : SearchTracksInteractor {

  override fun searchTracks(
    query: String,
    callback: (ArrayList<Track>) -> Unit,
  ) {

    scope.launch {
      val tracks = withContext(Dispatchers.IO) {
        repository.searchTracks(query) // Выполнение поиска треков в фоновом потоке
      }
      callback(tracks) // Вызываем callback с результатами поиска на главном потоке
    }
    // GlobalScope.launch(Dispatchers.IO) { // Запуск корутины на фоновом потоке (IO)
    //   val tracks = repository.searchTracks(query) // Выполнение поиска треков
    //   withContext(Dispatchers.Main) { // Возвращаемся на главный поток для обновления UI
    //     callback(tracks) // Вызываем callback с результатами поиска
    //   }
    // }
  }
}