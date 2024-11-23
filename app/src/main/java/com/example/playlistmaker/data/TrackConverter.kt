package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.FavoriteTrack
import com.example.playlistmaker.data.db.Playlist
import com.example.playlistmaker.data.db.TrackInPlaylist
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.model.PlaylistModel
import com.example.playlistmaker.domain.model.Track

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

    fun convertToTrackInPlaylist(track: Track, playlistId: Long): TrackInPlaylist {
        return TrackInPlaylist(
            trackId = track.trackId,
            playlistId = playlistId,
            artworkUrl = track.artworkUrl100 ?: "",
            trackName = track.trackName,
            artistName = track.artistName,
            albumName = track.collectionName,
            releaseYear = track.releaseDate?.substring(0, 4)?.toIntOrNull(),
            genre = track.primaryGenreName,
            country = track.country,
            duration = formatDuration(track.trackTimeMillis ?: 0),
            audioUrl = track.previewUrl ?: "",
            isFavorite = track.isFavorite,
            timeAdded = System.currentTimeMillis()
        )
    }


    // Конвертация TrackInPlaylist обратно в Track
    fun convertToDomainModel(trackInPlaylist: TrackInPlaylist): Track {
        return Track(
            trackId = trackInPlaylist.trackId,
            trackName = trackInPlaylist.trackName,
            artistName = trackInPlaylist.artistName,
            collectionName = trackInPlaylist.albumName,
            releaseDate = trackInPlaylist.releaseYear?.toString(),
            primaryGenreName = trackInPlaylist.genre,
            country = trackInPlaylist.country,
            trackTimeMillis = parseDuration(trackInPlaylist.duration ?: "00:00"),
            previewUrl = trackInPlaylist.audioUrl,
            isFavorite = trackInPlaylist.isFavorite,
            timeAdd = trackInPlaylist.timeAdded,
            artworkUrl100 = trackInPlaylist.artworkUrl
        )
    }


    fun fromEntity(entity: Playlist): PlaylistModel {
        return PlaylistModel(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            coverPath = entity.coverPath,
            trackCount = entity.trackCount,
            trackIds = entity.trackIds
        )
    }

    fun toEntity(domain: PlaylistModel): Playlist {
        return Playlist(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            coverPath = domain.coverPath,
            trackCount = domain.trackCount,
            trackIds = domain.trackIds
        )
    }

    private fun parseDuration(duration: String): Long {
        val parts = duration.split(":")
        val minutes = parts[0].toLongOrNull() ?: 0L
        val seconds = parts.getOrNull(1)?.toLongOrNull() ?: 0L
        return (minutes * 60 + seconds) * 1000
    }

    private fun formatDuration(milliseconds: Long): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

}