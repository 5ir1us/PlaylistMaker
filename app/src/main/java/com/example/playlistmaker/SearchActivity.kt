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
const val SEARCH_STRING_QUERY = "search_key"
class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView


    //    private lateinit var  simpleTextWatcher: TextWatcher
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchc)

        searchEditText = findViewById(R.id.search)
        clearButton = findViewById(R.id.clearIcon)
        val backButton = findViewById<ImageView>(R.id.buttonSittingBack)

//кнопка назад
        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            hideKeyboard()
        }



        if (savedInstanceState != null) {
            // Восстанавливаем текст из Bundle
            val text = savedInstanceState.getString(SEARCH_STRING_QUERY)
            // Устанавливаем текст в EditText
            searchEditText.setText(text)
        }



        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("DummyTextWatcher", "beforeTextChanged: " + s);

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("DummyTextWatcher", "onTextChanged: " + s);


            }

            override fun afterTextChanged(s: Editable?) {
                clearButton.visibility = clearButtonVisibility(s)
            }

        }
        searchEditText.addTextChangedListener(simpleTextWatcher)


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING_QUERY,searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchText = savedInstanceState.getString(SEARCH_STRING_QUERY)
        searchEditText.setText(searchText)


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

