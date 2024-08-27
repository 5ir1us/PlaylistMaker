package com.example.playlistmaker.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchcBinding
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.adapter.SearchHistoryAdapter
import com.example.playlistmaker.presentation.adapter.TrackAdapter
import com.example.playlistmaker.presentation.viewmodel.SearchViewModel

import kotlinx.coroutines.Runnable

class SearchActivity : AppCompatActivity() {

  private lateinit var simpleTextWatcher: TextWatcher
  private lateinit var binding: ActivitySearchcBinding
  private lateinit var trackAdapter: TrackAdapter
  private lateinit var historyAdapter: SearchHistoryAdapter

  private val handler = Handler(Looper.getMainLooper())
  private var isClickAllowed = true

  private val searchViewModel: SearchViewModel by viewModels {
    Creator.provideSearchViewModelFactory(this, lifecycleScope)
  }

  private val searchRunnable = Runnable {
    val query = binding.searchEdittext.text.toString().trim()
    if (query.isNotEmpty()) {
      searchViewModel.searchTracks(query)
    } else {
      searchViewModel.resetStateToHistory()
    }
  }

  companion object {
    const val CLICK_DEBOUNCE = 3000L // константа для отложки запроса
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySearchcBinding.inflate(layoutInflater)
    setContentView(binding.root)


    searchViewModel.searchScreenState.observe(this) { state ->
      binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

      binding.recyclerListTrack.visibility =
        if (state.searchResults.isNotEmpty()) View.VISIBLE else View.GONE

      binding.searchHistoryLayout.visibility =
        if (state.isHistoryVisible) View.VISIBLE else View.GONE

      binding.clearIcon.visibility = if (state.isClearIconVisible) View.VISIBLE else View.GONE

      binding.noResultsLayout.visibility = if (state.showNoResults) View.VISIBLE else View.GONE

      binding.noConnectionLayout.visibility = if (state.showError) View.VISIBLE else View.GONE

      trackAdapter.updateTracks(ArrayList(state.searchResults))
      historyAdapter.updateData(state.history.toMutableList())
    }

    //TextWatcher и обработка ввода текста
    simpleTextWatcher = object : TextWatcher {
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

        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, CLICK_DEBOUNCE)
        searchViewModel.onQueryTextChanged(s.toString())
      }
      override fun afterTextChanged(s: Editable?) {
        binding.clearIcon.visibility = clearButtonVisibility(s)
      }
    }



    binding.searchEdittext.addTextChangedListener(simpleTextWatcher)
    binding.searchEdittext.setOnFocusChangeListener { _, focus ->
      if (focus && binding.searchEdittext.text.isEmpty()) {
        searchViewModel.onSearchFocusChanged(true)
      } else {
        searchViewModel.onSearchFocusChanged(false)
      }

    }

    binding.buttonSittingBack.setOnClickListener {
      finish()
    }


    binding.clearIcon.setOnClickListener {
      hideKeyboard()
      binding.searchEdittext.setText("")
      binding.searchEdittext.clearFocus()
      searchViewModel.resetStateToHistory()
    }

    binding.recyclerListTrack.layoutManager = LinearLayoutManager(this@SearchActivity)
    trackAdapter = TrackAdapter(arrayListOf())
    binding.recyclerListTrack.adapter = trackAdapter

    binding.recyclerSearchHistory.layoutManager = LinearLayoutManager(this@SearchActivity)
    historyAdapter = SearchHistoryAdapter()
    binding.recyclerSearchHistory.adapter = historyAdapter

    binding.clearSearchHistory.setOnClickListener {
      searchViewModel.clearSearchHistory()
    }



    trackAdapter.setItemClickListener { track ->
      if (clickDebounce()) {
        searchViewModel.addTrackToHistory(track)
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

  private fun getInfoSong(
    intent: Intent,
    track: Track,
  ) {
    with(intent) {
      putExtra(AudioPlayerActivity.TRACK_INFO, track)

    }
  }
}