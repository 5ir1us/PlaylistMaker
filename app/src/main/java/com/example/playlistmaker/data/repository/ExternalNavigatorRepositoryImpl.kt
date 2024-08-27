package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.system.SystemNavigator
import com.example.playlistmaker.domain.repository.ExternalNavigatorRepository

class ExternalNavigatorRepositoryImpl(private val systemNavigator: SystemNavigator) :
  ExternalNavigatorRepository {

  override fun shareApp(shareText: String) {
    val shareIntent = systemNavigator.buildShareIntent(shareText)
    systemNavigator.startActivity(shareIntent)
  }

  override fun openSupport(mail: String, supportText: String, bodySupport: String) {
    val supportIntent = systemNavigator.buildSupportIntent(mail, supportText, bodySupport)
    systemNavigator.startActivity(supportIntent)
  }

  override fun openTerms(termsText: String) {
    val termsIntent = systemNavigator.buildTermsIntent(termsText)
    systemNavigator.startActivity(termsIntent)
  }
}



