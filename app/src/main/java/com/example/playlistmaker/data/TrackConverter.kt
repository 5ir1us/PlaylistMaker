package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.model.Track

object TrackConverter {

  fun convertToDomainModel(dto: TrackDto): Track {

    return Track(
      trackName = dto.trackName,
      artistName = dto.artistName,
      trackTimeMillis = dto.trackTimeMillis,
      artworkUrl100 = dto.artworkUrl100,
      collectionName = dto.collectionName,
      releaseDate = dto.releaseDate,
      primaryGenreName = dto.primaryGenreName,
      country = dto.country,
      previewUrl = dto.previewUrl
    )
  }
}