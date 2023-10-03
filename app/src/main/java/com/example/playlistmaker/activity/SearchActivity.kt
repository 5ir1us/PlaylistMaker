package com.example.playlistmaker.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Interface.RetrofitServices
import com.example.playlistmaker.Interface.SearchMethods
import com.example.playlistmaker.activity.RetrofitClient.retrofitService
import com.example.playlistmaker.adapter.TrackAdapter
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.databinding.ActivitySearchcBinding

class SearchActivity : AppCompatActivity() {

  private lateinit var mService: RetrofitServices
  private lateinit var simpleTextWatcher: TextWatcher
  private lateinit var binding: ActivitySearchcBinding
  private lateinit var trackAdapter: TrackAdapter
  private val searchMethods = SearchMethods ()
  private val trackList = ArrayList<Track>()


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySearchcBinding.inflate(layoutInflater)
    setContentView(binding.root)

    mService = retrofitService

    //интерфейс отслеживает нажатия
    simpleTextWatcher = object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int,
      ) {
        Log.d("DummyTextWatcher", "beforeTextChanged: " + s);
      }

      override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int,
      ) {
        Log.d("DummyTextWatcher", "onTextChanged: " + s);
      }

      override fun afterTextChanged(s: Editable?) {
        binding.clearIcon.visibility = clearButtonVisibility(s)
      }
    }

    binding.apply {
      buttonSittingBack.setOnClickListener {
        finish()
      }

      clearIcon.setOnClickListener {
        binding.searchEdittext.setText("")
        hideKeyboard()
        trackAdapter.updateTracks(trackList)

      }

      recyclerListTrack.layoutManager = LinearLayoutManager(this@SearchActivity)

      trackAdapter = TrackAdapter(trackList)

      recyclerListTrack.adapter = trackAdapter

      searchEdittext.addTextChangedListener(simpleTextWatcher)


      searchMethods.setSearchListener(searchEdittext, retrofitService,recyclerListTrack,
        trackAdapter,noConnectionLayout,noResultsLayout,retryButton,trackList)


    }
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

}

