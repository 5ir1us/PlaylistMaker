package com.example.playlistmaker.presentation.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.viewmodel.AudioPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {

    companion object {
        const val TRACK_INFO: String = "Track"
    }

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private val audioPlayerViewModel: AudioPlayerViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.buttonPlayerBack.setOnClickListener {
            findNavController().popBackStack()
        }


        //получает данные из поиска и
        val track = arguments?.getParcelable<Track>("TRACK_INFO")
        track?.let { setupTrackInfo(it) }
        observeViewModel()


        binding.playButton.setOnClickListener {
            track?.let { track ->
                track.previewUrl?.let { url ->
                    audioPlayerViewModel.togglePlayback(url)
                }
            }
        }

        binding.favoriteMusic.setOnClickListener {
            audioPlayerViewModel.toggleFavorite()
        }
    }

    private fun observeViewModel() {
        audioPlayerViewModel.screenState.observe(viewLifecycleOwner) { state ->
            Log.d("AudioPlayerFragment", "UI updating with currentTrackTime: ${state.currentTrackTime}")
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