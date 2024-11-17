package com.example.playlistmaker.domain.model

import android.os.Parcelable
import com.example.playlistmaker.data.db.FavoriteTrack
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,// Ссылка на изображение
    val trackId: Int,// идентификатор уникальности трека
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var isFavorite: Boolean = false,
    var timeAdd: Long
) : Parcelable {

    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
    fun getTimeTrack(): String? =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    fun changeDateFormat(): String {

        return try {
            val date = when {
                releaseDate.isNullOrEmpty() -> null
                releaseDate.length == 4 -> SimpleDateFormat("yyyy", Locale.getDefault()).parse(
                    releaseDate
                )

                releaseDate.length == 10 -> SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).parse(releaseDate)

                else -> SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(
                    releaseDate
                )
            }


            date?.let {
                SimpleDateFormat("yyyy", Locale.getDefault()).format(it)
            } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }


    }


}



