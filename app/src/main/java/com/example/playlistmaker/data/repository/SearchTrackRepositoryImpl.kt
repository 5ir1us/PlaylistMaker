package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.TrackConverter
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.repository.SearchTrackRepository
import com.example.playlistmaker.domain.model.Track


class SearchTrackRepositoryImpl(
  private val networkClient: NetworkClient
  // private val sharedPreferences: SharedPreferencesTrackHistory
) : SearchTrackRepository {

  override fun searchTracks(query: String): ArrayList<Track> {
    val request = TrackSearchRequest(query)
    val response = networkClient.doRequest(request)

    return if (response is TrackSearchResponse && response.resultCode == 200) {

       ArrayList(response.results.map { TrackConverter.convertToDomainModel(it) })
    } else {
      ArrayList()
    }

  }
}

  // override fun saveTrackToHistory(track: Track) {
  //   // Сохранение трека в историю
  // }
  //
  // override fun getTrackHistory(): ArrayList<Track> {
  //   // Получение истории треков
  // }
// }

