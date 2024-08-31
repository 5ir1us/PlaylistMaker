package com.example.playlistmaker.domain.repository

import android.content.Intent

interface ExternalNavigatorRepository {
  fun shareApp(shareText: String)
  fun openSupport(mail: String, supportText: String, bodySupport: String)
  fun openTerms(termsText: String)
}