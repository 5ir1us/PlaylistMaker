package com.example.playlistmaker.data.system

import android.content.Intent

interface SystemNavigator {
  fun startActivity(intent: Intent)
  fun buildShareIntent(shareText: String): Intent
  fun buildSupportIntent(mail: String, supportText: String, bodySupport: String): Intent
  fun buildTermsIntent(termsText: String): Intent
  fun getString(resId: Int): String
}