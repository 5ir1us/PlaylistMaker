package com.example.playlistmaker.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.utils.RequestOnServer
import com.example.playlistmaker.activity.RetrofitClient.retrofitService
import com.example.playlistmaker.adapter.SearchHistoryAdapter
import com.example.playlistmaker.adapter.TrackAdapter
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.databinding.ActivitySearchcBinding
import com.example.playlistmaker.utils.Constants
import com.example.playlistmaker.utils.SearchHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

  private lateinit var simpleTextWatcher: TextWatcher
  private lateinit var binding: ActivitySearchcBinding
  private lateinit var trackAdapter: TrackAdapter
  private lateinit var historyAdapter: SearchHistoryAdapter
  private val requestOnServer = RequestOnServer()
  private val trackList = ArrayList<Track>()
  private lateinit var searchHistor: SearchHistory
  private lateinit var sharedPreferences: SharedPreferences
  private lateinit var historyList: MutableList<Track>
  private lateinit var uniqueList: MutableList<Track>

  // Handler для реализации debounce
  private val handler = Handler(Looper.getMainLooper())
  private var isClickAllowed = true
  private val searchRunnable = Runnable {
    val query = binding.searchEdittext.text.toString().trim()
    if (query.isNotEmpty()) {
      binding.progressBar.visibility = View.VISIBLE
      requestOnServer.performSearch(
        this@SearchActivity,
        retrofitService,
        query,
        binding.recyclerListTrack,
        trackAdapter,
        binding.noConnectionLayout,
        binding.noResultsLayout,
        binding.retryButton,
        trackList
      )
    } else {
      binding.progressBar.visibility = View.GONE
      trackAdapter.updateTracks(ArrayList()) // Очистка списка треков
      showOrHideSearchHistoryLayout() // Показ истории поиска, если доступно
    }
  }

  companion object {
    const val CLICK_DEBOUNCE = 3000L // константа для отложки запроса
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySearchcBinding.inflate(layoutInflater)
    setContentView(binding.root)



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
          //TODO
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
        // searchEdittext.text.delete(0, searchEdittext.text.length)
        trackAdapter.updateTracks(trackList)
        historyAdapter.updateData(searchHistor.loadTrackFromSharedPreferences().toMutableList())
        showOrHideSearchHistoryLayout()
      }

      recyclerListTrack.layoutManager = LinearLayoutManager(this@SearchActivity)
      trackAdapter = TrackAdapter(trackList)
      recyclerListTrack.adapter = trackAdapter

      //выполняет запрос на сервер кнопкоц
      requestOnServer.setSearchListener(
        this@SearchActivity,//todo
        searchEdittext, retrofitService, recyclerListTrack,
        trackAdapter, noConnectionLayout, noResultsLayout, retryButton, trackList
      )

      // SEARCH history :)
      recyclerSearchHistory.layoutManager = LinearLayoutManager(this@SearchActivity)
      historyAdapter = SearchHistoryAdapter()
      historyList = historyAdapter.historyList
      recyclerSearchHistory.adapter = historyAdapter
      sharedPreferences = getSharedPreferences(Constants.SEARCH_HISTORY, Context.MODE_PRIVATE)
      searchHistor = SearchHistory(sharedPreferences)

      clearSearchHistory.setOnClickListener {
        searchHistor.clearSharedPreferencesHistory()
        historyAdapter.clearTracksHistory(historyList)
        showOrHideSearchHistoryLayout()
      }

      historyAdapter.updateData(searchHistor.loadTrackFromSharedPreferences().toMutableList())

      trackAdapter.setItemClickListener { track ->
        if (clickDebounce()) { //условие дебаунса

          historyList = searchHistor.loadTrackFromSharedPreferences().toMutableList()
          historyList.add(0, track)
          historyAdapter.notifyItemInserted(0)
        }
        uniqueList = historyList.toSet().toMutableList()
        if (historyList.size > 10) {
          historyList = historyList.subList(0, 10)
        }
        searchHistor.saveTrackToSharedPreferences(uniqueList)
        showOrHideSearchHistoryLayout()

        val searchIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
        getInfoSong(searchIntent, track)
        startActivity(searchIntent)
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
}

