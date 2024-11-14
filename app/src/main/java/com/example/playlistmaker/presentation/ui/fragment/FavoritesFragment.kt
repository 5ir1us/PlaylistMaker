package com.example.playlistmaker.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.adapter.TrackAdapter
import com.example.playlistmaker.presentation.viewmodel.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {


    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoriteTrackViewModel: FavoritesViewModel by viewModel()

    private lateinit var adapter: TrackAdapter




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
            // Отображение плейсхолдера, если список избранных пуст
            binding.emptyFavoriteIcon.visibility =
                if (favoriteTracks.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() {
        adapter = TrackAdapter(arrayListOf())
        binding.recyclerListFavoritesTrack.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerListFavoritesTrack.adapter = adapter

        // Установка слушателя для переключения состояния избранного
        adapter.setItemClickListener { track ->
            toggleFavorite(track)
        }
    }

    private fun toggleFavorite(track: Track) {
        if (track.isFavorite) {
            favoriteTrackViewModel.removeTrackFromFavorites(track)
        } else {
            favoriteTrackViewModel.addTrackToFavorites(track)
        }
        track.isFavorite = !track.isFavorite  // Локально обновляем состояние избранного
        adapter.notifyDataSetChanged()  // Обновляем адаптер для отображения изменений
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}