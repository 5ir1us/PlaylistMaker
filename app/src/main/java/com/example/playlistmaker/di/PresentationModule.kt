package com.example.playlistmaker.di


import com.example.playlistmaker.presentation.viewmodel.AudioPlayerViewModel
import com.example.playlistmaker.presentation.viewmodel.CreatePlaylistViewModel
import com.example.playlistmaker.presentation.viewmodel.EditPlaylistViewModel
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
            favoriteTracksInteractor = get(),
            playlistInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(
            searchTracksInteractor = get(),
            trackHistoryInteractor = get(),
            favoriteTracksInteractor = get()
        )
    }

    viewModel {
        SettingsViewModel(themeInteractor = get(), externalNavigatorInteractor = get())
    }

    viewModel {
        PlaylistsViewModel(
            playlistInteractor = get(),
            trackInPlaylistDao = get()  // Здесь передаем TrackInPlaylistDao
        )
    }


    viewModel { FavoritesViewModel(get()) }

    viewModel { CreatePlaylistViewModel(playlistInteractor =  get()) }

    viewModel { EditPlaylistViewModel(playlistInteractor = get()) }

}