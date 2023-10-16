package com.example.playlistmaker.utils

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapter.TrackAdapter
import com.example.playlistmaker.data.NewTrack
import com.example.playlistmaker.data.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestOnServer {
  // Функция для установки слушателя событий для поля ввода текста
  fun setSearchListener(
    editText: EditText,
    service: RetrofitServices,
    list: RecyclerView,
    adapter: TrackAdapter,
    noConnectionLayout: View,
    noResultsLayout: View,
    retryButtonn: ImageView,
    trackk: ArrayList<Track>,
  ) {
    editText.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        // Функция для выполнения запроса к сервису поиска треков
        performSearch(
          service, editText.text.toString(), list, adapter, noConnectionLayout, noResultsLayout,
          retryButtonn, trackk
        )
        true
      } else {
        false
      }
    }
  }

  // Функция для выполнения запроса
  private fun performSearch(
    service: RetrofitServices,
    query: String,
    list: RecyclerView,
    adapter: TrackAdapter,
    noConnectionLayout: View,
    noResultsLayout: View,
    retryButton: ImageView,
    trackk: ArrayList<Track>,
  ) {
    val call = service.findTrack(query)
    call.enqueue(object : Callback<NewTrack> {

      override fun onResponse(
        call: Call<NewTrack>,
        response: Response<NewTrack>,
      ) {
        if (response.code() != 200) {
          setVisibility(noConnectionLayout, list, noResultsLayout)

          retryButton.setOnClickListener {
            val newCall = call.clone()
            newCall.enqueue(this)
            setVisibility(list, noResultsLayout, noConnectionLayout)
          }
          return
        }
        when {
          response.body()?.results?.isEmpty() == true -> {

            setVisibility(noResultsLayout, list, noConnectionLayout)
          }

          response.body()?.results?.isNotEmpty() == true -> {

            updateListAndAdapter(response.body()?.results!!, adapter, trackk)
            setVisibility(list, noResultsLayout, noConnectionLayout)
          }
        }
      }

      override fun onFailure(
        call: Call<NewTrack>,
        t: Throwable,
      ) {
      }
    })
  }

  // Функция для установки видимости для разных макетов
  fun setVisibility(
    visibleView: View,
    vararg invisibleViews: View,
  ) {
    visibleView.visibility = View.VISIBLE
    for (view in invisibleViews) {
      view.visibility = View.GONE
    }
  }

  // Функция для добавления результатов к списку треков и обновления адаптера
  private fun updateListAndAdapter(
    results: ArrayList<Track>,
    adapter: TrackAdapter,
    track: ArrayList<Track>,
  ) {
    track.addAll(results)
    adapter.notifyDataSetChanged()
  }
}


