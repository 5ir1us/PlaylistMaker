package com.example.playlistmaker.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.presentation.adapter.PlaylistsAdapter
import com.example.playlistmaker.presentation.adapter.TrackAdapter
import com.example.playlistmaker.presentation.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {


  private var _binding: FragmentPlaylistsBinding? = null
  private val binding  get() = _binding!!
  private val playlistsViewModel: PlaylistsViewModel by viewModel()

  private lateinit var playlistsAdapter: PlaylistsAdapter

  companion object {
    fun newInstance() = PlaylistsFragment()

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
    return binding.root

   }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.newPlaylistButton.setOnClickListener {
      findNavController().navigate(R.id.action_mediaLibraryFragment_to_createFragment)
    }

    setupRecyclerView()
    observeViewModel()

  }

  private fun setupRecyclerView() {
    playlistsAdapter = PlaylistsAdapter(emptyList())
    binding.recyclerViewPlaylists.apply {
      layoutManager = GridLayoutManager(requireContext(), 2) // 2 колонки
      adapter = playlistsAdapter
    }
  }

  private fun observeViewModel() {
    playlistsViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
      playlistsAdapter.updateData(playlists)
      toggleEmptyState(playlists.isEmpty())
    }

    playlistsViewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
      toggleEmptyState(isEmpty)
    }
  }

  private fun toggleEmptyState(isEmpty: Boolean) {
    binding.emptyStateView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    binding.recyclerViewPlaylists.visibility = if (isEmpty) View.GONE else View.VISIBLE
  }

  override fun onResume() {
    super.onResume()
    playlistsViewModel.loadPlaylists()
  }
  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

}