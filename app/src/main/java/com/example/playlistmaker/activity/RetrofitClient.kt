package com.example.playlistmaker.activity

import android.app.Application
import com.example.playlistmaker.data.MusicApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitBuild : Application() {

    companion object {
        private const val URL_MUSIC_SEARCH = "https://itunes.apple.com"
    }

    lateinit var retrofit: Retrofit
    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit.Builder()
            .baseUrl(URL_MUSIC_SEARCH)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    }

}