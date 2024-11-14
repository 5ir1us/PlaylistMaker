package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.TrackConverter
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.repository.SearchTrackRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class SearchTrackRepositoryImpl(
  private val networkClient: NetworkClient
 ) : SearchTrackRepository {

    override fun searchTracks(query: String): Flow<List<Track>> = flow {
        val request = TrackSearchRequest(query)
        val response = networkClient.doRequest(request)

        if (response.resultCode == 200) {
             emit(response.results.map { TrackConverter.convertToDomainModel(it) })
        } else {
            emit(emptyList())
        }
    }
}

