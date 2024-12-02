package com.example.playlistmaker.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.presentation.adapter.PlaylistsAdapter
import com.example.playlistmaker.presentation.adapter.TrackAdapter
import com.example.playlistmaker.presentation.viewmodel.PlaylistsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

  private var isClickAllowed = true
  private var clickJob: Job? = null

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
    playlistsAdapter = PlaylistsAdapter(emptyList()) { playlist ->
      if (clickDebounce()) {

        val bundle = Bundle().apply {
          putParcelable("PLAYLIST_INFO", playlist)

        }
        findNavController().navigate(
          R.id.action_mediaLibraryFragment_to_playlistDetailsFragment,
          bundle
        )
      }
    }

    binding.recyclerViewPlaylists.apply {
      layoutManager = GridLayoutManager(requireContext(), 2)
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


  fun clickDebounce(): Boolean {
    val current = isClickAllowed
    if (isClickAllowed) {
      clickJob?.cancel()
      clickJob = viewLifecycleOwner.lifecycleScope.launch {
        isClickAllowed = false
        delay(1000L) // 1 секунда
        isClickAllowed = true
      }
    }
    return current
  }

}