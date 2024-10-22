package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchTrackRepository {
    fun searchTracks(query: String): Flow<List<Track>>
}