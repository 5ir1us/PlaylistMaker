package com.example.playlistmaker.data.dto

import ResponseCode

data class TrackSearchResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>,
) : ResponseCode()
