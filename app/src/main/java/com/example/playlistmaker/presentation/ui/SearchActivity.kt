package com.example.playlistmaker.presentation.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchcBinding
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.domain.interactor.SearchTracksInteractor
import com.example.playlistmaker.domain.interactor.TrackHistoryInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.adapter.SearchHistoryAdapter
import com.example.playlistmaker.presentation.adapter.TrackAdapter
import kotlinx.coroutines.Runnable

class SearchActivity : AppCompatActivity() {

  private lateinit var simpleTextWatcher: TextWatcher
  private lateinit var binding: ActivitySearchcBinding
  private lateinit var trackAdapter: TrackAdapter
  private lateinit var historyAdapter: SearchHistoryAdapter
  private val trackList = ArrayList<Track>()
  private lateinit var trackHistoryInteractor: TrackHistoryInteractor//todo
  private lateinit var historyList: MutableList<Track>
  private lateinit var searchTracksInteractor: SearchTracksInteractor

  // Handler для реализации debounce
  private val handler = Handler(Looper.getMainLooper())
  private var isClickAllowed = true

  private val searchRunnable = Runnable {
    val query = binding.searchEdittext.text.toString().trim()
    if (query.isNotEmpty()) {
      binding.progressBar.visibility = View.VISIBLE

      searchTracksAndUpdateUI(
        this@SearchActivity,
        query,
        binding.recyclerListTrack,
        trackAdapter,
        binding.noConnectionLayout,
        binding.noResultsLayout,
        binding.retryButton,
        trackList,
        searchTracksInteractor
      )
    } else {
      binding.progressBar.visibility = View.GONE
      trackAdapter.updateTracks(ArrayList()) // Очистка списка треков
      showOrHideSearchHistoryLayout() //   истории поиска, если доступно
    }
  }

  companion object {
    const val CLICK_DEBOUNCE = 3000L // константа для отложки запроса
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySearchcBinding.inflate(layoutInflater)
    setContentView(binding.root)

    trackHistoryInteractor = Creator.provideTrackHistoryInteractor(this)

    searchTracksInteractor = Creator.provideTrackInteractor(lifecycleScope)

    binding.apply {

      simpleTextWatcher = object : TextWatcher {
        //TextWatcher и обработка ввода текста
        override fun beforeTextChanged(
          s: CharSequence?,
          start: Int,
          count: Int,
          after: Int,
        ) {
        }

        override fun onTextChanged(
          s: CharSequence?,
          start: Int,
          before: Int,
          count: Int,
        ) {
          if (searchEdittext.hasFocus() && s?.isEmpty() == true) {
            showOrHideSearchHistoryLayout()
          } else {
            showOrHideSearchHistoryLayout()
          }
          handler.removeCallbacks(searchRunnable)
          handler.postDelayed(searchRunnable, CLICK_DEBOUNCE)
        }

        override fun afterTextChanged(s: Editable?) {
          clearIcon.visibility = clearButtonVisibility(s)
        }
      }

      searchEdittext.addTextChangedListener(simpleTextWatcher)
      searchEdittext.setOnFocusChangeListener { _, focus ->
        if (focus && searchEdittext.text.isEmpty()) {
          showOrHideSearchHistoryLayout()
        } else {
          showOrHideSearchHistoryLayout()
        }

      }


      buttonSittingBack.setOnClickListener {
        finish()
      }

      clearIcon.setOnClickListener {
        hideKeyboard()
        searchEdittext.setText("")
        searchEdittext.clearFocus()
        trackAdapter.updateTracks(trackList)
        historyAdapter.updateData(trackHistoryInteractor.getHistory().toMutableList())
        showOrHideSearchHistoryLayout()
      }

      recyclerListTrack.layoutManager = LinearLayoutManager(this@SearchActivity)
      trackAdapter = TrackAdapter(trackList)
      recyclerListTrack.adapter = trackAdapter

      // SEARCH history :)
      recyclerSearchHistory.layoutManager = LinearLayoutManager(this@SearchActivity)
      historyAdapter = SearchHistoryAdapter()
      historyList = historyAdapter.historyList
      recyclerSearchHistory.adapter = historyAdapter



      clearSearchHistory.setOnClickListener {
        trackHistoryInteractor.clearHistory()
        historyAdapter.clearTracksHistory(historyList)
        showOrHideSearchHistoryLayout()
      }

       historyAdapter.updateData(
        trackHistoryInteractor.getHistory().toMutableList()
      )



      trackAdapter.setItemClickListener { track ->
        if (clickDebounce()) {
          // todo Должно использоваться через TrackHistoryInteractor
          trackHistoryInteractor.addTrackToHistory(
            track
          ) // Сохраняем трек в историю через интерактор
          historyAdapter.updateData(
            trackHistoryInteractor.getHistory().toMutableList()
          ) // Обновляем адаптер истории
          showOrHideSearchHistoryLayout() // Обновляем видимость истории поиска

          val searchIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
          getInfoSong(searchIntent, track)
          startActivity(searchIntent)
        }
      }
      historyAdapter.setItemClickListener { track ->
        val historyIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)

        getInfoSong(historyIntent, track)
        startActivity(historyIntent)
      }

    }
  }

  // дебаунс проверка запроса
  private fun clickDebounce(): Boolean {
    val current = isClickAllowed
    if (isClickAllowed) {
      isClickAllowed = false
      handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE)
    }
    return current
  }

  //проврка пустого запроса
  private fun clearButtonVisibility(input: CharSequence?): Int =
    if (input.isNullOrEmpty()) {
      View.GONE
    } else {
      View.VISIBLE
    }

  //скрытие клавиатуры
  private fun hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as
      InputMethodManager

    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
  }

  private fun showOrHideSearchHistoryLayout() {
    binding.apply {
      if (historyList.size > 0 && searchEdittext.text.isEmpty()) {

        searchHistoryLayout.visibility = View.VISIBLE
        noConnectionLayout.visibility = View.GONE
        noResultsLayout.visibility = View.GONE
        recyclerListTrack.visibility = View.GONE
      } else {
        searchHistoryLayout.visibility = View.GONE
      }
    }
  }

  private fun getInfoSong(
    intent: Intent,
    track: Track,
  ) {
    with(intent) {
      putExtra(AudioPlayerActivity.TRACK_INFO, track)

    }
  }

  fun searchTracksAndUpdateUI(
    context: Context,
    query: String,
    list: RecyclerView,
    adapter: TrackAdapter,
    noConnectionLayout: View,
    noResultsLayout: View,
    retryButton: ImageView,
    trackk: ArrayList<Track>,
    searchTracksInteractor: SearchTracksInteractor, // добавляем интерактор как зависимость
  ) {
    val progressBar = (context as Activity).findViewById<ProgressBar>(R.id.progressBar)
    progressBar.visibility = View.VISIBLE

    //   интерактор для поиска треков
    searchTracksInteractor.searchTracks(query) { tracks ->
      progressBar.visibility = View.GONE

      if (tracks.isEmpty()) {
        //   список треков пуст, показываем макет отсутствия результатов
        setVisibility(noResultsLayout, list, noConnectionLayout)
      } else {
        //   треки найдены, обновляем адаптер и отображаем список
        updateListAndAdapter(tracks, adapter, trackk)
        setVisibility(list, noResultsLayout, noConnectionLayout)
      }
    }

    // Обработка повторной попытки
    retryButton.setOnClickListener {
      searchTracksAndUpdateUI(
        context, query, list, adapter, noConnectionLayout, noResultsLayout, retryButton, trackk,
        searchTracksInteractor
      )
    }
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