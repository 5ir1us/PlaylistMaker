package com.example.playlistmaker.utils

import com.example.playlistmaker.data.NewTrack
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("/search?entity=song")
    fun findTrack(@Query("term") text: String) : Call<NewTrack>
}
