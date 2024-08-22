package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.repository.SearchTrackRepositoryImpl
import com.example.playlistmaker.data.repository.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.SharedPreferencesProviderImpl
import com.example.playlistmaker.data.repository.ThemeRepositoryImpl
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.interactor.SearchTracksInteractor
import com.example.playlistmaker.domain.repository.SearchTrackRepository
import com.example.playlistmaker.domain.interactor.ThemeInteractor
import com.example.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.domain.repository.SharedPreferencesProvider
import com.example.playlistmaker.domain.repository.ThemeRepository
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import kotlinx.coroutines.CoroutineScope

object Creator {

  //  экземпляр NetworkClient через RetrofitClient
  private fun createNetworkClient(): NetworkClient {
    return RetrofitClient()  // Теперь здесь создается и возвращается NetworkClient
  }

  // Создаем и предоставляем экземпляр репозитория
  private fun getTrackRepository(): SearchTrackRepository {
    val networkClient = createNetworkClient()
    return SearchTrackRepositoryImpl(networkClient)
  }

  //   поиск
  fun provideTrackInteractor(scope: CoroutineScope): SearchTracksInteractor {
    val repository = getTrackRepository()
    return SearchTracksInteractorImpl(scope, repository)
  }

  // история
  private fun getTrackHistoryRepository(sharedPreferences: SharedPreferences): TrackHistoryRepository {
    return TrackHistoryRepositoryImpl(sharedPreferences)
  }

  //  история
  fun provideTrackHistoryInteractor(sharedPreferences: SharedPreferences): TrackHistoryInteractorImpl {
    return TrackHistoryInteractorImpl(getTrackHistoryRepository(sharedPreferences))
  }

  //шаред
  private fun getSharedPreferencesProvider(context: Context): SharedPreferencesProvider {
    return SharedPreferencesProviderImpl(context)
  }

  //     настройки
  private fun getThemeRepository(context: Context): ThemeRepository {
    return ThemeRepositoryImpl(getSharedPreferencesProvider(context).provideSharedPreferences())
  }

  //   настройки
  fun provideThemeInteractor(context: Context): ThemeInteractor {
    return ThemeInteractorImpl(getThemeRepository(context))
  }

  //плеер
  fun providePlayTrackInteractor(onTrackComplete: () -> Unit): AudioPlayerInteractor {
    // todo это экземляр  AudioPlayerRepositoryImpl в случаае чего раздели
    val trackPlayerRepository = AudioPlayerRepositoryImpl(onTrackComplete)
    return AudioPlayerInteractorImpl(trackPlayerRepository)
  }
}