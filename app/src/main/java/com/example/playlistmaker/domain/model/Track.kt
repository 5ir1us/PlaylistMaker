package com.example.playlistmaker.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Locale
@Parcelize
data class Track(
  val trackName: String,
  val artistName: String,
  val trackTimeMillis: Long?,
  val artworkUrl100: String?,// Ссылка на изображение
  // val trackId: Int,// идентификатор уникальности трека
  val collectionName: String?,
  val releaseDate: String?,
  val primaryGenreName: String?,
  val country: String?,
  val previewUrl: String?
):Parcelable {

  fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
  fun getTimeTrack(): String? =
    SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

  fun changeDateFormat(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val date = releaseDate?.let { inputFormat.parse(it) }
    val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    return outputFormat.format(date)
  }
}



