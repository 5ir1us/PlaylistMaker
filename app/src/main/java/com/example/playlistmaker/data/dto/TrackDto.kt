package com.example.playlistmaker.data.dto

import kotlinx.parcelize.Parcelize

// Класс TrackDto

data class TrackDto(
  val trackName: String,
  val artistName: String,
  val trackTimeMillis: Long?,
  val artworkUrl100: String?,
  val trackId: Int,
  val collectionName: String?,
  val releaseDate: String?,
  val primaryGenreName: String?,
  val country: String?,
  val previewUrl: String?

)

// fun TrackDto.toDomainModel(): Track {
//   return Track(
//     trackName = this.trackName,
//     artistName = this.artistName,
//     trackTimeMillis = this.trackTimeMillis,
//     artworkUrl100 = this.artworkUrl100,
//     collectionName = this.collectionName,
//     releaseDate = this.releaseDate,
//     primaryGenreName = this.primaryGenreName,
//     country = this.country,
//     previewUrl = this.previewUrl
//   )
// }