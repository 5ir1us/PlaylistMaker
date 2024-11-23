package com.example.playlistmaker.data.network


import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext


class RetrofitClient(private val trackApiService: TrackApiService) : NetworkClient {


    override suspend fun doRequest(dto: Any): TrackSearchResponse {
        return withContext(Dispatchers.IO) {
            if (dto is TrackSearchRequest) {
                try {
                    val response = trackApiService.findTrack(dto.term)
                    if (response.isSuccessful) {
                        response.body()?.apply { resultCode = 200 } ?: TrackSearchResponse(resultCount = 0).apply {
                            resultCode = 500 // Ошибка: пустой результат
                        }
                    } else {
                        TrackSearchResponse(resultCount = 0).apply {
                            resultCode = response.code() // Код ошибки сервера
                        }
                    }
                } catch (e: Exception) {
                    TrackSearchResponse(resultCount = 0).apply {
                        resultCode = 500 // Ошибка сети
                    }
                }
            } else {
                TrackSearchResponse(resultCount = 0).apply {
                    resultCode = 400 // Неверный запрос
                }
            }
        }
    }
}
