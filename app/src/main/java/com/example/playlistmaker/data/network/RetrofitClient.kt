package com.example.playlistmaker.data.network

import ResponseCode
import android.util.Log
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitClient ( private val trackApiService: TrackApiService): NetworkClient {

  private val BASE_URL = "https://itunes.apple.com"
  private val retrofit: Retrofit by lazy {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }


  override fun doRequest(dto: Any): ResponseCode {
    return if (dto is TrackSearchRequest) {
      try {
        // Выполнение синхронного запроса
        val response = trackApiService.findTrack(dto.term).execute()
        val body = response.body() ?: ResponseCode()
        body.apply { resultCode = response.code() }
      } catch (e: IOException) {
        // Логирование ошибки
        Log.e("RetrofitClient", "Network request failed", e)
        ResponseCode().apply { resultCode = 500 } // Возвращаем 500 в случае ошибки
      }
    } else {
      ResponseCode().apply { resultCode = 400 }
    }
  }
}
