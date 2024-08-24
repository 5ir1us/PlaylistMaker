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
import com.example.playlistmaker.domain.Constants.PREFS_NAME
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
  private fun getTrackHistoryRepository(preferencesProvider: SharedPreferencesProvider ): TrackHistoryRepository {
    return TrackHistoryRepositoryImpl(preferencesProvider)
  }

  //  история
  fun provideTrackHistoryInteractor(context: Context): TrackHistoryInteractorImpl {
    val preferencesRepository = providePreferencesRepository(context)
    return TrackHistoryInteractorImpl(getTrackHistoryRepository(preferencesRepository))
  }



  //шаред
  private fun getSharedPreferencesProvider(context: Context): SharedPreferences {
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  }
  fun providePreferencesRepository(context: Context): SharedPreferencesProvider {
    val sharedPreferences = getSharedPreferencesProvider(context)
    return SharedPreferencesProviderImpl(sharedPreferences)
  }




  //     настройки
  private fun getThemeRepository(preferencesRepository: SharedPreferencesProvider): ThemeRepository {
    return ThemeRepositoryImpl(preferencesRepository)
  }

  //   настройки
  fun provideThemeInteractor(context: Context): ThemeInteractor {
    val preferencesRepository = providePreferencesRepository(context)
    return ThemeInteractorImpl(getThemeRepository(preferencesRepository))
  }



  //плеер
  fun providePlayTrackInteractor(onTrackComplete: () -> Unit): AudioPlayerInteractor {
    // todo это экземляр  AudioPlayerRepositoryImpl в случаае чего раздели
    val trackPlayerRepository = AudioPlayerRepositoryImpl(onTrackComplete)
    return AudioPlayerInteractorImpl(trackPlayerRepository)
  }
}