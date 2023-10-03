package com.example.playlistmaker.Interface

import com.example.playlistmaker.data.NewTrack
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitServices {

    @GET("/search")
    fun findTrack(@Query("term") text: String) : Call<NewTrack>
}
