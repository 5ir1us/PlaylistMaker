package com.example.playlistmaker.data.system

import android.content.Context
import android.content.Intent
import android.net.Uri

class SystemNavigatorImpl(private val context: Context) : SystemNavigator {
  override fun startActivity(intent: Intent) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
  }

  override fun buildShareIntent(shareText: String): Intent {
    return Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(Intent.EXTRA_TEXT, shareText)
    }
  }

  override fun buildSupportIntent(mail: String, supportText: String, bodySupport: String): Intent {
    val uri = Uri.Builder()
      .scheme("mailto")
      .appendPath(mail)
      .appendQueryParameter("subject", supportText)
      .appendQueryParameter("body", bodySupport)
      .build()

    return Intent(Intent.ACTION_SENDTO).apply {
      data = uri
    }
  }

  override fun buildTermsIntent(termsText: String): Intent {
    return Intent(Intent.ACTION_VIEW).apply {
      data = Uri.parse(termsText)
    }
  }

  override fun getString(resId: Int): String {
    return context.getString(resId)
  }
}