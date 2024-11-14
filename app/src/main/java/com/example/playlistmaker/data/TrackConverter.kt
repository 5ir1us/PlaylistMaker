package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.FavoriteTrack
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.model.Track
import okhttp3.internal.concurrent.formatDuration

object TrackConverter {

    fun convertToDomainModel(dto: TrackDto): Track {

        return Track(
            trackName = dto.trackName,
            artistName = dto.artistName,
            trackTimeMillis = dto.trackTimeMillis,
            artworkUrl100 = dto.artworkUrl100,
            trackId = dto.trackId,
            collectionName = dto.collectionName,
            releaseDate = dto.releaseDate,
            primaryGenreName = dto.primaryGenreName,
            country = dto.country,
            previewUrl = dto.previewUrl,
            isFavorite = dto.isFavorite,
            timeAdd = dto.timeAdd
        )
    }

    fun convertToFavoriteEntity(dto: TrackDto): FavoriteTrack {
        return FavoriteTrack(
            trackId = dto.trackId,
            artworkUrl = dto.artworkUrl100 ?: "",
            trackName = dto.trackName,
            artistName = dto.artistName,
            albumName = dto.collectionName,
            releaseYear = dto.releaseDate?.substring(0, 4)?.toIntOrNull() ?: 0,
            genre = dto.primaryGenreName ?: "",
            country = dto.country ?: "",
            duration = formatDuration(dto.trackTimeMillis ?: 0),
            audioUrl = dto.previewUrl ?: "",
            inFavorite = dto.isFavorite,
            timeAdd = System.currentTimeMillis()
        )
    }

    fun convertToDomainModel(favorite: FavoriteTrack): Track {
        return Track(
            trackId = favorite.trackId,
            artworkUrl100 = favorite.artworkUrl,
            trackName = favorite.trackName,
            artistName = favorite.artistName,
            collectionName = favorite.albumName,
            releaseDate = favorite.releaseYear.toString(),
            primaryGenreName = favorite.genre,
            country = favorite.country,
            trackTimeMillis = parseDuration(favorite.duration),
            previewUrl = favorite.audioUrl,
            isFavorite = favorite.inFavorite,
            timeAdd = favorite.timeAdd
        )
    }

    fun convertToDto(track: Track): TrackDto {
        return TrackDto(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = track.isFavorite,
            timeAdd = track.timeAdd

        )
    }

    private fun parseDuration(duration: String): Long {
        val parts = duration.split(":")
        val minutes = parts[0].toLongOrNull() ?: 0L
        val seconds = parts.getOrNull(1)?.toLongOrNull() ?: 0L
        return (minutes * 60 + seconds) * 1000
    }


}