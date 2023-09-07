package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchcBinding


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_STRING_QUERY = "search_key"
    }

    private lateinit var simpleTextWatcher: TextWatcher
    private lateinit var binding: ActivitySearchcBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchcBinding.inflate(layoutInflater)
        setContentView(binding.root)

//кнопка назад
        binding.buttonSittingBack.setOnClickListener {
            finish()
        }

        binding.clearIcon.setOnClickListener {
            binding.search.setText("")
            hideKeyboard()
        }

        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("DummyTextWatcher", "beforeTextChanged: " + s);

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("DummyTextWatcher", "onTextChanged: " + s);

            }

            override fun afterTextChanged(s: Editable?) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
            }
        }

        binding.search.addTextChangedListener(simpleTextWatcher)

        // определили как будет отоб. список
        binding.recyclerListTrack.layoutManager = LinearLayoutManager(this)

        val tracks = ArrayList<Track>()

        // Добавляем треки
        tracks.add(Track("Smells Like Teen Spirit", "Nirvana", "5:01", "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"))
        tracks.add(Track("Billie Jean", "Michael Jackson", "4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"))
        tracks.add(Track("Stayin' Alive", "Bee Gees", "4:10", "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"))
        tracks.add(Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"))
        tracks.add(Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"))

        val trackAdapter = TrackAdapter(tracks)
        binding.recyclerListTrack.adapter = trackAdapter

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING_QUERY, binding.search.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchText = savedInstanceState.getString(SEARCH_STRING_QUERY)
        binding.search.setText(searchText)

    }

    //проврка пустого запроса
    private fun clearButtonVisibility(s: CharSequence?): Int {

        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    //скрытие клавиатуры
    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

}

