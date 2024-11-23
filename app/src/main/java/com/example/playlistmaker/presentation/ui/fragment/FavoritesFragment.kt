package com.example.playlistmaker.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.adapter.TrackAdapter
import com.example.playlistmaker.presentation.viewmodel.FavoritesViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {


    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoriteTrackViewModel: FavoritesViewModel by viewModel()

    private lateinit var adapter: TrackAdapter

    private var isClickAllowed = true
    private var clickJob: Job? = null

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }

        private const val CLICK_DEBOUNCER_FAVOR = 1000L
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Подписка на обновления избранных треков
        favoriteTrackViewModel.favoriteTracks.observe(viewLifecycleOwner) { favoriteTracks ->
            adapter.updateTracks(ArrayList(favoriteTracks))
            binding.recyclerListFavoritesTrack.visibility =
                if (favoriteTracks.isNotEmpty()) View.VISIBLE else View.GONE
            binding.emptyFavoriteIcon.visibility =
                if (favoriteTracks.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() {
        adapter = TrackAdapter(arrayListOf())
        binding.recyclerListFavoritesTrack.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerListFavoritesTrack.adapter = adapter

        // Установка слушателя для переключения состояния избранного
//        adapter.setItemClickListener { track ->
//            toggleFavorite(track)
//        }

        adapter.setItemClickListener { track ->
            if (clickDebounce()) { //   дебаунс
                //    передаем трек
                val bundle = Bundle().apply {
                    putParcelable("TRACK_INFO", track) // Добавляем трек с ключом "TRACK_INFO"
                }

                // Переход
                findNavController().navigate(
                    R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
                    bundle
                )
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // Функция для проверки и предотвращения многократного нажатия (дебаунс)
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            clickJob?.cancel()
            clickJob = viewLifecycleOwner.lifecycleScope.launch {
                isClickAllowed = false
                delay(CLICK_DEBOUNCER_FAVOR)
                isClickAllowed = true
            }
        }
        return current
    }
}