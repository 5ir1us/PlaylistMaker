package com.example.playlistmaker.domain.repository


interface ExternalNavigatorRepository {
  fun shareApp(shareText: String)
  fun openSupport(mail: String, supportText: String, bodySupport: String)
  fun openTerms(termsText: String)
}