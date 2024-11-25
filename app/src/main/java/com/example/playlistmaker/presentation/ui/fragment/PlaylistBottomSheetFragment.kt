package com.example.playlistmaker.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentBottomSheetBinding
import com.example.playlistmaker.domain.model.PlaylistModel
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.adapter.PlaylistBottomSheetAdapter
import com.example.playlistmaker.presentation.viewmodel.AudioPlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PlaylistBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistAdapter: PlaylistBottomSheetAdapter

    private val audioPlayerViewModel: AudioPlayerViewModel by sharedViewModel()

    companion object {
        const val TRACK_KEY = "TRACK_INFO"
        fun newInstance(track: Track): PlaylistBottomSheetFragment {
            val args = Bundle().apply {
                putParcelable(TRACK_KEY, track)
            }
            val fragment = PlaylistBottomSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = arguments?.getParcelable<Track>(TRACK_KEY)


        // Обработка кнопки создания нового плейлиста
        binding.newPlaylistButton.setOnClickListener {
            Toast.makeText(requireContext(), "Create new playlist clicked", Toast.LENGTH_SHORT)
            dismiss()
            findNavController().navigate(R.id.action_audioPlayerFragment_to_createFragment) // Логика перехода на экран создания нового плейлиста
        }


        if (track != null) {
            playlistAdapter = PlaylistBottomSheetAdapter(emptyList()) { playlist ->
                addTrackToPlaylist(track, playlist)
            }
        }

        setupRecyclerView(track)
        audioPlayerViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            playlistAdapter.updatePlaylists(playlists)
        }
        loadPlaylists()


    }

    private fun setupRecyclerView(track: Track?) {
        val recyclerView = binding.playlistRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        playlistAdapter = PlaylistBottomSheetAdapter(emptyList()) { playlist ->
            track?.let {
                addTrackToPlaylist(it, playlist)
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    "No track available for this action.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        recyclerView.adapter = playlistAdapter
    }


    private fun updatePlaylists(playlists: List<PlaylistModel>) {
        playlists.forEach { playlist ->
            Log.d(
                "PlaylistBottomSheet",
                "Playlist: id=${playlist.id}, name=${playlist.name}, trackCount=${playlist.trackCount}"
            )
        }

        playlistAdapter.updatePlaylists(playlists)
    }

    private fun loadPlaylists() {
        lifecycleScope.launch {
            audioPlayerViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
                updatePlaylists(playlists)
            }
        }
    }

    private fun addTrackToPlaylist(track: Track, playlist: PlaylistModel) {
        lifecycleScope.launch {
            val isTrackInPlaylist =
                audioPlayerViewModel.isTrackInPlaylist(track.trackId, playlist.id)

            if (isTrackInPlaylist) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_track_error, playlist.name),
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                audioPlayerViewModel.addTrackToPlaylist(track, playlist)
                dismiss()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_added_to_playlist, playlist.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //Задвижка
    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)

            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.6).toInt()

            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.isHideable = true
            behavior.isFitToContents = false
            behavior.halfExpandedRatio = 0.9f // 90% для half-expanded

            bottomSheet.clipToOutline = false
            dialog.window?.setDimAmount(0.5f)
            bottomSheet.elevation = 0f
            bottomSheet.outlineProvider = null

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            behavior.peekHeight =
                                (resources.displayMetrics.heightPixels * 0.6).toInt()
                        }

                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            bottomSheet.layoutParams.height =
                                (resources.displayMetrics.heightPixels * 0.9).toInt()
                        }

                        BottomSheetBehavior.STATE_EXPANDED -> {

                            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.RoundedBottomSheetDialog
    }
}
