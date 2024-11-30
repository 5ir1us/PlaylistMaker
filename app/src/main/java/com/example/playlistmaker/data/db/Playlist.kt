package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String?,
    val coverPath: String?, // Путь к файлу изображения обложки
    val trackIds: String = "[]", // Список идентификаторов треков, пустой по умолчанию
    val trackCount: Int = 0 // Количество треков в плейлисте
)