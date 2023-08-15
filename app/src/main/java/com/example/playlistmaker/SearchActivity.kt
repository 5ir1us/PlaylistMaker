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

