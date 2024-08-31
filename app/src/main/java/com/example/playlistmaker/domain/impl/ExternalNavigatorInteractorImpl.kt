package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.interactor.ExternalNavigatorInteractor
import com.example.playlistmaker.domain.repository.ExternalNavigatorRepository

class ExternalNavigatorInteractorImpl(
  private val repository: ExternalNavigatorRepository,
  private val shareText: String,
  private val mail: String,
  private val supportText: String,
  private val bodySupport: String,
  private val termsText: String
) : ExternalNavigatorInteractor {

  override fun shareApp( ) {
    repository.shareApp(shareText)
  }

  override fun openSupport( ) {
    repository.openSupport(mail, supportText, bodySupport)
  }

  override fun openTerms( ) {
    repository.openTerms(termsText)
  }
}