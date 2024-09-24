package com.example.playlistmaker.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.adapter.SearchHistoryAdapter
import com.example.playlistmaker.presentation.adapter.TrackAdapter
import com.example.playlistmaker.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    companion object {
        private const val CLICK_DEBOUNCER = 3000L // Значение задержки в миллисекундах
    }

    private lateinit var simpleTextWatcher: TextWatcher
    private lateinit var binding: FragmentSearchBinding


    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: SearchHistoryAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private val searchViewModel: SearchViewModel by viewModel()

    private val searchRunnable = kotlinx.coroutines.Runnable {
        val query = binding.searchEdittext.text.toString().trim()
        if (query.isNotEmpty()) {
            searchViewModel.searchTracks(query)
        } else {
            searchViewModel.resetStateToHistory()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.searchScreenState.observe(viewLifecycleOwner) { state ->

            binding.progressBar.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            binding.recyclerListTrack.visibility =
                if (state.searchResults.isNotEmpty()) View.VISIBLE else View.GONE

            binding.searchHistoryLayout.visibility =
                if (state.isHistoryVisible) View.VISIBLE else View.GONE

            binding.clearIcon.visibility =
                if (state.isClearIconVisible) View.VISIBLE else View.GONE

            binding.noResultsLayout.visibility =
                if (state.showNoResults) View.VISIBLE else View.GONE

            binding.noConnectionLayout.visibility =
                if (state.showError) View.VISIBLE else View.GONE

            trackAdapter.updateTracks(ArrayList(state.searchResults))
            historyAdapter.updateData(state.history.toMutableList())
        }

        setupUI()


        trackAdapter.setItemClickListener { track ->
            if (clickDebounce()) {
                searchViewModel.addTrackToHistory(track)

                val bundle = Bundle().apply {
                    putParcelable("TRACK_INFO", track)
                }
                findNavController().navigate(
                    R.id.action_searchFragment_to_audioPlayerFragment,
                    bundle
                )
            }
        }

        historyAdapter.setItemClickListener { track ->
            val bundle = Bundle().apply {
                putParcelable("TRACK_INFO", track)
            }
            findNavController().navigate(R.id.action_searchFragment_to_audioPlayerFragment, bundle)
        }

//        // TODO:
//        trackAdapter.setItemClickListener { track ->
//            if (clickDebounce()) {
//                searchViewModel.addTrackToHistory(track)
//                val searchIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
//                getInfoSong(searchIntent, track)
//                startActivity(searchIntent)
//            }
//        }

//        historyAdapter.setItemClickListener { track ->
//            val historyIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
//            getInfoSong(historyIntent, track)
//            startActivity(historyIntent)
//        }
    }

    private fun setupUI() {
        // TextWatcher для обработки ввода текста
        simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(searchRunnable)
                handler.postDelayed(searchRunnable, CLICK_DEBOUNCER)
                searchViewModel.onQueryTextChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
            }
        }

        binding.searchEdittext.addTextChangedListener(simpleTextWatcher)

        // Обработка изменения фокуса в поле поиска
        binding.searchEdittext.setOnFocusChangeListener { _, focus ->
            if (focus && binding.searchEdittext.text.isEmpty()) {
                searchViewModel.onSearchFocusChanged(true)
            } else {
                searchViewModel.onSearchFocusChanged(false)
            }
        }

        binding.clearIcon.setOnClickListener {
            hideKeyboard()
            binding.searchEdittext.setText("")
            binding.searchEdittext.clearFocus()
            searchViewModel.resetStateToHistory()
        }

        // Настройка адаптеров
        binding.recyclerListTrack.layoutManager = LinearLayoutManager(requireContext())
        trackAdapter = TrackAdapter(arrayListOf())
        binding.recyclerListTrack.adapter = trackAdapter

        binding.recyclerSearchHistory.layoutManager = LinearLayoutManager(requireContext())
        historyAdapter = SearchHistoryAdapter()
        binding.recyclerSearchHistory.adapter = historyAdapter

        binding.clearSearchHistory.setOnClickListener {
            searchViewModel.clearSearchHistory()
        }
    }

    // Функция для проверки и предотвращения многократного нажатия (дебаунс)
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCER)
        }
        return current
    }

    // Скрытие кнопки очистки
    private fun clearButtonVisibility(input: CharSequence?): Int =
        if (input.isNullOrEmpty()) View.GONE else View.VISIBLE

    // Скрытие клавиатуры
    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun getInfoSong(intent: Intent, track: Track) {
        with(intent) {
            putExtra(AudioPlayerFragment.TRACK_INFO, track)// TODO:
        }
    }
}