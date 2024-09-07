package com.example.playlistmaker.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.presentation.adapter.TrackAdapter

class PlaylistsFragment : Fragment() {
  private lateinit var binding: FragmentPlaylistsBinding

  private lateinit var  trackAdapter:TrackAdapter

  companion object {
    fun newInstance(): PlaylistsFragment {
      return PlaylistsFragment()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
     return inflater.inflate(R.layout.fragment_playlists, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)


  }


}