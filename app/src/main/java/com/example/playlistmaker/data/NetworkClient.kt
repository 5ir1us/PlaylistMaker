package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackSearchResponse


interface NetworkClient {
    suspend fun doRequest(dto: Any): TrackSearchResponse
}