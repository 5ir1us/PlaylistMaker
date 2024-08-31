package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.repository.SearchTrackRepositoryImpl
import com.example.playlistmaker.data.repository.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.repository.AudioPlayerRepositoryImpl
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
import com.example.playlistmaker.data.datasource.SharedPreferencesDataSource
import com.example.playlistmaker.data.repository.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.data.system.SystemNavigator
import com.example.playlistmaker.data.system.SystemNavigatorImpl
import com.example.playlistmaker.domain.impl.ExternalNavigatorInteractorImpl
import com.example.playlistmaker.domain.interactor.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.repository.ThemeRepository
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.example.playlistmaker.presentation.viewmodel.AudioPlayerViewModelFactory
import com.example.playlistmaker.presentation.viewmodel.SearchViewModelFactory
import com.example.playlistmaker.presentation.viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.CoroutineScope

object Creator {

  private lateinit var applicationContext: Context

  fun init(context: Context) {
    applicationContext = context.applicationContext
  }

  private fun createNetworkClient(): NetworkClient {
    return RetrofitClient()
  }

  // экземпляра SearchTrackRepository
  private fun getTrackRepository(): SearchTrackRepository {
    val networkClient = createNetworkClient()
    return SearchTrackRepositoryImpl(networkClient)
  }

  //   поиск
  fun provideTrackInteractor( ): SearchTracksInteractor {
    val repository = getTrackRepository()
    return SearchTracksInteractorImpl(  repository)
  }

  // история
  private fun getTrackHistoryRepository(): TrackHistoryRepository {
    val preferencesProvider = provideSharedPreferencesDataSource()
    return TrackHistoryRepositoryImpl(preferencesProvider)
  }

  //  история
  fun provideTrackHistoryInteractor(): TrackHistoryInteractorImpl {
    return TrackHistoryInteractorImpl(getTrackHistoryRepository())
  }

  //шаред
  private fun provideSharedPreferencesDataSource(): SharedPreferencesDataSource {
    val sharedPreferences =
      applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return SharedPreferencesDataSource(sharedPreferences)
  }

  //  настройки
  private fun provideThemeRepository(): ThemeRepository {
    val preferencesRepository = provideSharedPreferencesDataSource()
    return ThemeRepositoryImpl(preferencesRepository)
  }

  //   настройки
  private fun provideThemeInteractor(): ThemeInteractor {
    return ThemeInteractorImpl(provideThemeRepository())
  }

  private fun provideSystemNavigator(): SystemNavigator {
    return SystemNavigatorImpl(applicationContext)
  }

  // ExternalNavigatorInteractor
  private fun provideExternalNavigatorInteractor(systemNavigator: SystemNavigator): ExternalNavigatorInteractor {
    val repository = ExternalNavigatorRepositoryImpl(systemNavigator)
    val shareText = systemNavigator.getString(R.string.share_message)
    val mail = systemNavigator.getString(R.string.mail_support)
    val bodySupport = systemNavigator.getString(R.string.body_support)
    val supportText = systemNavigator.getString(R.string.message_support)
    val termsText = systemNavigator.getString(R.string.terms_user_message)

    return ExternalNavigatorInteractorImpl(
      repository, shareText, mail, supportText, bodySupport, termsText
    )
  }

  //ViewModel
  fun provideSettingsViewModelFactory(): SettingsViewModelFactory {
    val themeInteractor = provideThemeInteractor()
    val systemNavigator = provideSystemNavigator()
    val externalNavigatorInteractor = provideExternalNavigatorInteractor(systemNavigator)
    return SettingsViewModelFactory(themeInteractor, externalNavigatorInteractor)
  }

  // Интерактор для плеера
  private fun providePlayTrackInteractor(): AudioPlayerInteractor {
    return AudioPlayerInteractorImpl(AudioPlayerRepositoryImpl())
  }

  // Фабрика для ViewModel
  fun provideAudioPlayerViewModelFactory(): AudioPlayerViewModelFactory {
    val audioPlayerInteractor = providePlayTrackInteractor()
    return AudioPlayerViewModelFactory(audioPlayerInteractor)
  }

  fun provideSearchViewModelFactory(): SearchViewModelFactory {
    val searchTracksInteractor = provideTrackInteractor()
    val trackHistoryInteractor = provideTrackHistoryInteractor()
    return SearchViewModelFactory(searchTracksInteractor, trackHistoryInteractor)
  }
}


