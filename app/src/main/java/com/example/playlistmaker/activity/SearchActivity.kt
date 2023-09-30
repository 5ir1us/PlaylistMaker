package com.example.playlistmaker.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Interface.RetrofitServices
import com.example.playlistmaker.activity.RetrofitClient.retrofitService
import com.example.playlistmaker.adapter.TrackAdapter
import com.example.playlistmaker.data.NewTrack
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.databinding.ActivitySearchcBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

  private lateinit var mService: RetrofitServices
  private lateinit var simpleTextWatcher: TextWatcher
  private lateinit var binding: ActivitySearchcBinding
  private val track = ArrayList<Track>()
  private lateinit var trackAdapter: TrackAdapter

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
        Log.e("DummyTextWatcher", "beforeTextChanged: " + s);
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
        trackAdapter.updateTracks(track)//!!!!!!!!!!!!!!!

      }
      searchEdittext.addTextChangedListener(simpleTextWatcher)

      recyclerListTrack.layoutManager = LinearLayoutManager(this@SearchActivity)

      trackAdapter = TrackAdapter(track)

      recyclerListTrack.adapter = trackAdapter


      searchEdittext.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {

          val call = mService.findTrack(searchEdittext.text.toString())

          call.enqueue(object : Callback<NewTrack> {
            @SuppressLint("NotifyDataSetChanged")//подавить предупреждения
            override fun onResponse(
              call: Call<NewTrack>,
              response: Response<NewTrack>,
            ) {
              // если код ответа не равен 200, показываем экран без подключения и выходим из метода
              if (response.code() != 200) {
                setVisibility(noConnectionLayout, recyclerListTrack, noResultsLayout)
                retryButton.setOnClickListener {
                  val newCall = call.clone()
                  newCall.enqueue(this)
                  setVisibility(recyclerListTrack, noResultsLayout, noConnectionLayout)
                }
                return
              }

              when {
                response.body()?.results?.isEmpty() == true -> {

                  setVisibility(noResultsLayout, recyclerListTrack, noConnectionLayout)
                }

                response.body()?.results?.isNotEmpty() == true -> {

                  track.addAll(response.body()?.results ?: emptyList())
                  trackAdapter.notifyDataSetChanged()
                  setVisibility(recyclerListTrack, noResultsLayout, noConnectionLayout)
                }
              }
            }

            override fun onFailure(
              call: Call<NewTrack>,
              t: Throwable,
            ) {
              Toast.makeText(
                this@SearchActivity, "Произошла ошибка: ${t.message}", Toast.LENGTH_SHORT
              ).show()
            }
          })
          true
        }
        false
      }

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

  //показывает один экран и скрывает другие
  fun setVisibility(
    show: View,
    hide1: View,
    hide2: View,
  ) {
    show.visibility = View.VISIBLE // Показать виджет
    hide1.visibility = View.GONE // Скрыть виджет
    hide2.visibility = View.GONE // Скрыть виджет
  }
}


