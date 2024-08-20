package com.example.playlistmaker.domain

 import android.content.SharedPreferences
 import com.example.playlistmaker.data.NetworkClient
 import com.example.playlistmaker.data.repository.SearchTrackRepositoryImpl
 import com.example.playlistmaker.data.local.TrackHistoryRepositoryImpl
 import com.example.playlistmaker.data.network.RetrofitClient
 import com.example.playlistmaker.data.repository.ThemeRepositoryImpl
 import com.example.playlistmaker.domain.api.SearchTracksInteractor
import com.example.playlistmaker.domain.api.SearchTrackRepository
 import com.example.playlistmaker.domain.api.ThemeInteractor
 import com.example.playlistmaker.domain.interactor.TrackHistoryInteractor
 import com.example.playlistmaker.domain.impl.SearchTracksInteractorImpl
 import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
 import com.example.playlistmaker.domain.repository.ThemeRepository
 import com.example.playlistmaker.domain.repository.TrackHistoryRepository

object Creator {

 //  экземпляр NetworkClient через RetrofitClient
 private fun createNetworkClient(): NetworkClient {
  return RetrofitClient()  // Теперь здесь создается и возвращается NetworkClient
 }

 // Создаем и предоставляем экземпляр репозитория
 private fun getTrackRepository(): SearchTrackRepository {
  val networkClient = createNetworkClient()  // Используем созданный NetworkClient
  return SearchTrackRepositoryImpl(networkClient)  // Создаем репозиторий, передавая NetworkClient
 }

 //   экземпляр интерактора
 fun provideTrackInteractor(): SearchTracksInteractor {
  return SearchTracksInteractorImpl(getTrackRepository())
 }
//todo история поиска отработать
 // кземпляр репозитория для истории треков
 private fun getTrackHistoryRepository(sharedPreferences: SharedPreferences): TrackHistoryRepository {
  return TrackHistoryRepositoryImpl(sharedPreferences)
 }

 //  экземпляр интерактора для истории треков
 fun provideTrackHistoryInteractor(sharedPreferences: SharedPreferences): TrackHistoryInteractor {
  return TrackHistoryInteractor(getTrackHistoryRepository(sharedPreferences))
 }

 //   экземпляр ThemeRepository
 private fun getThemeRepository(sharedPreferences: SharedPreferences): ThemeRepository {
  return ThemeRepositoryImpl(sharedPreferences)
 }

 // экземпляр ThemeInteractor
 fun provideThemeInteractor(sharedPreferences: SharedPreferences): ThemeInteractor {
  return ThemeInteractorImpl(getThemeRepository(sharedPreferences))
 }
}