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

  private fun createNetworkClient(): NetworkClient {
    return RetrofitClient()
  }

  // экземпляра SearchTrackRepository
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
  private fun getTrackHistoryRepository(preferencesProvider: SharedPreferencesDataSource): TrackHistoryRepository {
    return TrackHistoryRepositoryImpl(preferencesProvider)
  }

  //  история
  fun provideTrackHistoryInteractor(context: Context): TrackHistoryInteractorImpl {
    val preferencesRepository = provideSharedPreferencesDataSource(context)
    return TrackHistoryInteractorImpl(getTrackHistoryRepository(preferencesRepository))
  }

  //шаред
  private fun provideSharedPreferencesDataSource(context: Context): SharedPreferencesDataSource {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return SharedPreferencesDataSource(sharedPreferences)
  }

  //  настройки
  private fun provideThemeRepository(preferencesRepository: SharedPreferencesDataSource): ThemeRepository {
    return ThemeRepositoryImpl(preferencesRepository)
  }

  //   настройки
  private fun provideThemeInteractor(context: Context): ThemeInteractor {
    val preferencesRepository = provideSharedPreferencesDataSource(context)
    return ThemeInteractorImpl(provideThemeRepository(preferencesRepository))
  }

  private fun provideSystemNavigator(context: Context): SystemNavigator {
    return SystemNavigatorImpl(context)
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
  fun provideSettingsViewModelFactory(context: Context): SettingsViewModelFactory {
    val themeInteractor = provideThemeInteractor(context)
    val systemNavigator = provideSystemNavigator(context)
    val externalNavigatorInteractor = provideExternalNavigatorInteractor(systemNavigator)
    return SettingsViewModelFactory(themeInteractor, externalNavigatorInteractor)
  }

  //плеер
  fun providePlayTrackInteractor(onTrackComplete: () -> Unit): AudioPlayerInteractor {
    val trackPlayerRepository = AudioPlayerRepositoryImpl(onTrackComplete)
    return AudioPlayerInteractorImpl(trackPlayerRepository)
  }

  fun provideSearchViewModelFactory(context: Context,scope: CoroutineScope): SearchViewModelFactory {
    val searchTracksInteractor = provideTrackInteractor(scope)
    val trackHistoryInteractor = provideTrackHistoryInteractor(context)
    return SearchViewModelFactory(searchTracksInteractor, trackHistoryInteractor)
  }
}


