package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.datasource.SharedPreferencesDataSource
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.network.TrackApiService
import com.example.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.ExternalNavigatorRepositoryImpl
import com.example.playlistmaker.data.repository.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.domain.repository.PlaylistRepository
import com.example.playlistmaker.data.repository.SearchTrackRepositoryImpl
import com.example.playlistmaker.data.repository.ThemeRepositoryImpl
import com.example.playlistmaker.data.repository.TrackHistoryRepositoryImpl
import com.example.playlistmaker.data.system.SystemNavigator
import com.example.playlistmaker.data.system.SystemNavigatorImpl
import com.example.playlistmaker.domain.Constants.APP_PREFERENCES
import com.example.playlistmaker.data.repository.PlaylistRepositoryImpl
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.domain.repository.ExternalNavigatorRepository
import com.example.playlistmaker.domain.repository.FavoriteTrackRepository
import com.example.playlistmaker.domain.repository.SearchTrackRepository
import com.example.playlistmaker.domain.repository.ThemeRepository
import com.example.playlistmaker.domain.repository.TrackHistoryRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    single { SharedPreferencesDataSource(get()) }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<TrackApiService> {
        get<Retrofit>().create(TrackApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitClient(get())
    }

    single { Gson() }

    factory { MediaPlayer() }

    factory<AudioPlayerRepository> { AudioPlayerRepositoryImpl(get()) }

    single<ExternalNavigatorRepository> { ExternalNavigatorRepositoryImpl(get()) }

    factory<SearchTrackRepository> { SearchTrackRepositoryImpl(get()) }

    single<ThemeRepository> { ThemeRepositoryImpl(get()) }

    single<TrackHistoryRepository> { TrackHistoryRepositoryImpl(get(), get()) }

    single<SystemNavigator> { SystemNavigatorImpl(androidContext()) }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "app_database"
        )
//            .addMigrations(MIGRATION_3_4)
            .fallbackToDestructiveMigration()
            .build()

    }

    single<FavoriteTrackRepository> { FavoriteTrackRepositoryImpl(get()) }


    single<PlaylistRepository> { PlaylistRepositoryImpl(get(), get()) }
    single { get<AppDatabase>().favoriteTrackDao() }
    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().trackInPlaylistDao() }
}