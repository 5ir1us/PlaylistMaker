package com.example.playlistmaker.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.model.PlaylistModel
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.adapter.TrackAdapter
import com.example.playlistmaker.presentation.ui.activity.MainActivity
import com.example.playlistmaker.presentation.viewmodel.PlaylistsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val playlistInteractor: PlaylistInteractor by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.hideBottomNavigation()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)








        arguments?.getParcelable<PlaylistModel>("PLAYLIST_INFO")?.let { playlist ->
            setupUI(playlist)
            playlistsViewModel.loadTracksForPlaylist(playlist.id)
            binding.root.tag = playlist
        }




        setupBottomSheet()
        setupRecyclerView()
        setupToolbar()
        observeViewModel()
    }

    override fun onPause() {
        super.onPause()
        Log.d("PlaylistDetailsFragment", "Paused")

    }

    override fun onResume() {
        super.onResume()
        playlistsViewModel.loadPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val existingFragment = parentFragmentManager.findFragmentByTag("ShareBottomSheet")
        if (existingFragment is BottomSheetDialogFragment) {
            Log.d("PlaylistDetailsFragment", "Closing BottomSheetShareFragment on destroy")
            existingFragment.dismiss()
        }
        _binding = null

    }


    private fun setupBottomSheet() {

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        // Увеличиваем начальный размер на 50% высоты экрана
        val screenHeight = resources.displayMetrics.heightPixels
        bottomSheetBehavior.peekHeight = (screenHeight * 0.33).toInt()
        bottomSheetBehavior.isFitToContents = true
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setupRecyclerView() {
        playlistsViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            val trackAdapter = TrackAdapter(ArrayList(tracks))
            binding.recyclerViewTracks.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = trackAdapter
            }
            trackAdapter.setItemClickListener { track ->
                navigateToAudioPlayer(track)
            }
            trackAdapter.setItemLongClickListener { track ->
                val currentPlaylist = binding.root.tag as? PlaylistModel
                currentPlaylist?.let {
                    showDeleteConfirmationDialog(track, it)
                }
            }
        }
    }

    private fun navigateToAudioPlayer(track: Track) { // TODO:
        val bundle = Bundle().apply {
            putParcelable(AudioPlayerFragment.TRACK_INFO, track)
        }
        findNavController().navigate(
            R.id.action_playlistDetailsFragment_to_audioPlayerFragment,
            bundle
        )
    }

    private fun setupToolbar() {
        binding.toolbarPlaylistDetails.setNavigationOnClickListener {
            findNavController().popBackStack()

        }
    }

    private fun setupUI(playlist: PlaylistModel) {
        Log.d("PlaylistDetailsFragment", "Updating UI with playlist: $playlist")
        binding.namePlayList.text = playlist.name
        binding.playlistDescription.text = playlist.description
        Glide.with(this)
            .load(playlist.coverPath ?: R.drawable.placeholder)
            .into(binding.imagePlayList)

        binding.optionPlayList.setOnClickListener {
            showBottomSheetShare(playlist)
        }



        binding.sharePlayList.setOnClickListener {
            playlistsViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
                if (tracks.isEmpty()) {

                } else {
                    val message = createShareMessage(playlist, tracks)
                    sharePlaylist(message)
                }
            }
        }
        binding.namePlayList.invalidate()
        binding.playlistDescription.invalidate()

    }


    private fun observeViewModel() {
        playlistsViewModel.playlists.observe(viewLifecycleOwner) { playlists ->
            val currentPlaylist = playlists.find { it.id == (binding.root.tag as PlaylistModel).id }
            currentPlaylist?.let {
                setupUI(it)
            }

        }
        playlistsViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            updateTracksUI(tracks)

        }

    }

    private fun updateTracksUI(tracks: List<Track>) {
        val totalDurationMillis = tracks.sumOf { it.trackTimeMillis ?: 0L }
        val totalDurationMinutes = totalDurationMillis / 60000

        val durationText = resources.getQuantityString(
            R.plurals.total_duration,
            totalDurationMinutes.toInt(),
            totalDurationMinutes.toInt()
        )

        binding.duration.text = durationText

        val trackCountText = resources.getQuantityString(
            R.plurals.track_count,
            tracks.size,
            tracks.size
        )
        binding.count.text = trackCountText
    }

    // TODO: rrrrrrrrrrr
    private fun showBottomSheetShare(playlist: PlaylistModel) {
        playlistsViewModel.tracks.observe(viewLifecycleOwner) { tracks ->

            val existingFragment = parentFragmentManager.findFragmentByTag("ShareBottomSheet")

            if (existingFragment != null) {
                (existingFragment as BottomSheetDialogFragment).dismiss()

            }

            val bottomSheetShare = BottomSheetShareFragment.newInstance(playlist, tracks, this)
            bottomSheetShare.show(parentFragmentManager, "ShareBottomSheet")
        }
    }


    private fun showDeleteConfirmationDialog(track: Track, playlist: PlaylistModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track_title))
            .setMessage(getString(R.string.delete_track_message))
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                // Вызываем метод интерактора для удаления трека
                lifecycleScope.launch {
                    playlistInteractor.removeTrackFromPlaylist(track, playlist)
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun createShareMessage(playlist: PlaylistModel, tracks: List<Track>): String {
        val message = StringBuilder()
        message.append("${playlist.name}\n") // Название плейлиста
        message.append("${playlist.description}\n\n") // Описание
        message.append(
            "[${tracks.size}] ${
                resources.getQuantityString(
                    R.plurals.track_count,
                    tracks.size,
                    tracks.size
                )
            }\n\n"
        )

        // Пронумерованный список треков
        tracks.forEachIndexed { index, track ->
            val duration = track.getTimeTrack() ?: "00:00"
            message.append("${index + 1}. ${track.artistName} - ${track.trackName} ($duration)\n")
        }

        return message.toString()
    }

    private fun sharePlaylist(message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        // Запускаем Intent
        startActivity(Intent.createChooser(intent, getString(R.string.share_playlist_title)))
    }

    fun deleteCurrentPlaylist(playlist: PlaylistModel) {
        lifecycleScope.launch {
            try {

                // Удаление плейлиста через интерактор
                playlistInteractor.deletePlaylist(playlist)

                // Закрываем активный BottomSheetShareFragment
                val existingFragment = parentFragmentManager.findFragmentByTag("ShareBottomSheet")
                if (existingFragment is BottomSheetDialogFragment) {
                    Log.d("PlaylistDetailsFragment", "Closing BottomSheetShareFragment")
                    existingFragment.dismiss()
                }

                // Уведомление об успешном удалении
                Toast.makeText(
                    requireContext(),
                    getString(R.string.playlist_deleted),
                    Toast.LENGTH_SHORT
                ).show()

                // Переход на экран "Медиатека"
                findNavController().navigate(
                    R.id.action_playlistDetailsFragment_to_mediaLibraryFragment
                )

            } catch (e: Exception) {
                // Обработка ошибки
                Log.e("PlaylistDetailsFragment", "Error deleting playlist", e)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_deleting_playlist),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }


    }


}
