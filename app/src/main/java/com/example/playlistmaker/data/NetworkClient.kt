package com.example.playlistmaker.data

import ResponseCode

interface NetworkClient {
    fun doRequest(dto: Any): ResponseCode
}