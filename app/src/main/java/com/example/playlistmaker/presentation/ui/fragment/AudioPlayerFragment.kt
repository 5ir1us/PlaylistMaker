package com.example.playlistmaker.presentation.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.adapter.PlaylistBottomSheetAdapter
import com.example.playlistmaker.presentation.ui.activity.MainActivity
import com.example.playlistmaker.presentation.viewmodel.AudioPlayerViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {

    companion object {
        const val TRACK_INFO: String = "TRACK_INFO"
    }

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private val audioPlayerViewModel: AudioPlayerViewModel by viewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var playlistAdapter: PlaylistBottomSheetAdapter

    private lateinit var bottomSheetDialog: BottomSheetDialog


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as? MainActivity)?.hideBottomNavigation()



        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)













        binding.toolbarNewPlaylist.setOnClickListener {
            findNavController().popBackStack()
        }


        //получает данные из поиска
        val track = arguments?.getParcelable<Track>(TRACK_INFO)
        track?.let {
            setupTrackInfo(it)
            audioPlayerViewModel.checkIfFavorite(it) // TODO:
        }
        observeViewModel()


        binding.playButton.setOnClickListener {
            track?.let { track ->
                track.previewUrl?.let { url ->
                    audioPlayerViewModel.togglePlayback(url)
                }
            }
        }

        // Обновляем состояние кнопки "Нравится" в зависимости от того, является ли трек избранным
        audioPlayerViewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.favoriteMusic.setImageResource(
                if (isFavorite) R.drawable.like_button else R.drawable.favorite_border
            )
        }

        // Обработка клика по кнопке "Нравится"
        binding.favoriteMusic.setOnClickListener {
            track?.let { audioPlayerViewModel.toggleFavorite(it) }
        }


        audioPlayerViewModel.addTrackStatus.observe(viewLifecycleOwner) { isAdded ->
            if (isAdded) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_add_track),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.the_track_already_exists),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.newPlaylistButton.setOnClickListener {
            navigateToCreatePlaylistScreen()
        }

        setupBottomSheet() // TODO:  
        setupRecyclerView()
        loadPlaylists()

        binding.addToPlaylistButton.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun observeViewModel() {
        audioPlayerViewModel.screenState.observe(viewLifecycleOwner) { state ->

            binding.timeTrack.text = state.currentTrackTime
            binding.playButton.setImageResource(
                if (state.isPlaying) R.drawable.stop_player else R.drawable.play_button
            )
            binding.favoriteMusic.setImageResource(
                if (state.isFavorite) R.drawable.like_button else R.drawable.favorite_border
            )
        }
    }

    private fun setupTrackInfo(track: Track) {
        val radiusImage = dpToPx(8f, requireContext())
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(radiusImage))
            .into(binding.imageAlbum)

        binding.trackDuration.text = track.getTimeTrack()
        binding.albumOneName.text = track.collectionName
        binding.trackYear.text = track.changeDateFormat()
        binding.trackGenre.text = track.primaryGenreName
        binding.trackCountry.text = track.country
        binding.viewArtistName.text = track.artistName
        binding.albumArtist.text = track.trackName
        binding.timeTrack.text = "00:00"
        binding.visibleGroup.isVisible = track.collectionName != null
    }

    private fun setupBottomSheet() {
        val bottomSheetContainer = binding.playlistsBottomSheet
        val overlay = binding.overlay



          bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            peekHeight = (resources.displayMetrics.heightPixels * 0.5).toInt()
            isHideable = true
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = (slideOffset + 1) / 2
            }
        })
    }

    private fun showBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }


    private fun setupRecyclerView() {
        val recyclerView = binding.playlistRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        playlistAdapter = PlaylistBottomSheetAdapter(emptyList()) { playlist ->
            val track = arguments?.getParcelable<Track>(TRACK_INFO)
            track?.let {

                lifecycleScope.launch {
                    val isTrackInPlaylist =
                        audioPlayerViewModel.isTrackInPlaylist(it.trackId, playlist.id)

                    if (isTrackInPlaylist) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.toast_track_error, playlist.name),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        audioPlayerViewModel.addTrackToPlaylist(it, playlist)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.toast_added_to_playlist, playlist.name),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        recyclerView.adapter = playlistAdapter
    }


    private fun loadPlaylists() {
        lifecycleScope.launch {
            audioPlayerViewModel.getPlaylists().collect { playlists ->
                playlistAdapter.updatePlaylists(playlists)
            }
        }
    }

    private fun navigateToCreatePlaylistScreen() {
        findNavController().navigate(R.id.action_audioPlayerFragment_to_createFragment)
    }


    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    override fun onPause() {
        super.onPause()
        audioPlayerViewModel.pauseTrack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        audioPlayerViewModel.stopTrack()
    }
}