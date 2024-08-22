package com.example.playlistmaker.presentation.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.domain.interactor.ThemeInteractor

class SettingsActivity : AppCompatActivity() {

  private lateinit var themeInteractor: ThemeInteractor

  private lateinit var binding: ActivitySettingsBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // val app = application as App

    binding = ActivitySettingsBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //инициализ черз креатор
    themeInteractor = Creator.provideThemeInteractor(this)

    binding.themeSwitcher.isChecked = themeInteractor.isDarkThemeEnabled()

    //строковые ресурсы черех getstring
    val context: Context = this
    val shareText = context.getString(R.string.share_message)
    val mail = context.getString(R.string.mail_support)
    val bodySupport = context.getString(R.string.body_support)
    val supportText = context.getString(R.string.message_support)
    val termsText = context.getString(R.string.terms_user_message)




    binding.apply {

      //выход из настроек
      binding.buttonSittingBack.setOnClickListener {
        finish()
      }

      //переключатель
      binding.themeSwitcher.isChecked = themeInteractor.isDarkThemeEnabled()
      binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        themeInteractor.setDarkThemeEnabled(isChecked)
      }

      //поделиться приложением
      share.setOnClickListener {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(shareIntent)

      }
      //написать в пподдержку
      val builder = Uri.Builder() //собрал uri/, пример из практикума не работает
      builder.scheme("mailto")
        .appendPath(mail)
        .appendQueryParameter(
          "subject", supportText
        )
        .appendQueryParameter("body", bodySupport)
      val uri = builder.build()

      messageSupport.setOnClickListener {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = uri
        startActivity(emailIntent)
      }

      //пользовательское соглашение
      termsUser.setOnClickListener {
        val agreementIntent = Intent(Intent.ACTION_VIEW)
        agreementIntent.data = Uri.parse(termsText)
        startActivity(agreementIntent)
      }
    }
  }
}