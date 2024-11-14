package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.presentation.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.presentation.viewmodel.FavoritesViewModel
import com.example.playlistmaker.presentation.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.presentation.viewmodel.SearchViewModel
import com.example.playlistmaker.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        AudioPlayerViewModel(
            audioPlayerInteractor = get(),
            favoriteTracksInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(searchTracksInteractor = get(), trackHistoryInteractor = get())
    }

    viewModel {
        SettingsViewModel(themeInteractor = get(), externalNavigatorInteractor = get())
    }

    viewModel { PlaylistsViewModel() }


    viewModel { FavoritesViewModel(get()) }
}