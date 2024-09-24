package com.example.playlistmaker.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.isDarkThemeEnabled.observe(viewLifecycleOwner) { isEnabled ->
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


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}