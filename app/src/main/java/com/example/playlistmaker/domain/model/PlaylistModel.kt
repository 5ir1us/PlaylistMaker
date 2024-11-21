package com.example.playlistmaker.domain.model

data class PlaylistModel(
    val id: Long,
    val name: String,
    val description: String?,
    val coverPath: String?,
    val trackCount: Int
)
