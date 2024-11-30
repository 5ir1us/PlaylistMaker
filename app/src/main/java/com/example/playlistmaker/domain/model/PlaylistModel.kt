package com.example.playlistmaker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistModel(
    val id: Long,
    val name: String,
    val description: String?,
    val coverPath: String?,
    val trackCount: Int,
    val trackIds: String
): Parcelable
