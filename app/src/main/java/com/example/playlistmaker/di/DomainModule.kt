package com.example.playlistmaker.di

import com.example.playlistmaker.R
import com.example.playlistmaker.domain.Constants
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.impl.ExternalNavigatorInteractorImpl
import com.example.playlistmaker.domain.impl.SearchTracksInteractorImpl
import com.example.playlistmaker.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.interactor.AudioPlayerInteractor
import com.example.playlistmaker.domain.interactor.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.interactor.SearchTracksInteractor
import com.example.playlistmaker.domain.interactor.ThemeInteractor
import com.example.playlistmaker.domain.interactor.TrackHistoryInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


val domainModule = module {

  single(named("mail")) { androidContext().getString(R.string.mail_support) }
  single(named("shareText")) { androidContext().getString(R.string.share_message) }
  single(named("supportText")) { androidContext().getString(R.string.message_support_api) }
  single(named("bodySupport")) { androidContext().getString(R.string.body_support) }
  single(named("termsText")) { androidContext().getString(R.string.terms_user_message) }

  single<AudioPlayerInteractor> { AudioPlayerInteractorImpl(get()) }

  factory<ExternalNavigatorInteractor> {
    ExternalNavigatorInteractorImpl(
      repository = get(),
      shareText = get(named("shareText")),
      mail = get(named("mail")),
      supportText = get(named("supportText")),
      bodySupport = get(named("bodySupport")),
      termsText = get(named("termsText"))
    )
  }

  factory<SearchTracksInteractor> {
    SearchTracksInteractorImpl(repository = get())
  }

  factory<ThemeInteractor> {
    ThemeInteractorImpl(repository = get())
  }

  factory<TrackHistoryInteractor> {
    TrackHistoryInteractorImpl(repository = get())
  }

}
