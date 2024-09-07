package com.example.playlistmaker.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

  private lateinit var binding: ActivitySettingsBinding
  private val settingsViewModel: SettingsViewModel by  viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivitySettingsBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //   изменения состояния темы
    settingsViewModel.isDarkThemeEnabled.observe(this@SettingsActivity) { isEnabled ->
      binding.themeSwitcher.isChecked = isEnabled
      AppCompatDelegate.setDefaultNightMode(
        if (isEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
      )
    }

    // Обработка переключателя темы
    binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
      settingsViewModel.onThemeSwitchToggled(isChecked)
    }

    // Поделиться приложением
    binding.share.setOnClickListener {
      settingsViewModel.shareApp()
    }

    // Написать в поддержку
    binding.messageSupport.setOnClickListener {
      settingsViewModel.openSupport()
    }

    // Пользовательское соглашение
    binding.termsUser.setOnClickListener {
      settingsViewModel.openTerms()
    }

    //выход из настроек
    binding.buttonSittingBack.setOnClickListener {
      finish()
    }
  }
}
