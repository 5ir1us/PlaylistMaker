package com.example.playlistmaker.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
 import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.PlaylistModel
import com.example.playlistmaker.domain.model.Track
 import com.google.android.material.bottomsheet.BottomSheetBehavior
 import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class BottomSheetShareFragment(
    private val parentFragmentCallback: PlaylistDetailsFragment
) : BottomSheetDialogFragment() {

    private lateinit var playlist: PlaylistModel
    private var tracks: List<Track> = emptyList()

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val PLAYLIST_KEY = "PLAYLIST_KEY"
        private const val TRACKS_KEY = "TRACKS_KEY"

        fun newInstance(
            playlist: PlaylistModel,
            tracks: List<Track>,
            parentFragment: PlaylistDetailsFragment
        ): BottomSheetShareFragment {
            return BottomSheetShareFragment(parentFragment).apply {
                arguments = Bundle().apply {
                    putParcelable(PLAYLIST_KEY, playlist)
                    putParcelableArrayList(TRACKS_KEY, ArrayList(tracks))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playlist = arguments?.getParcelable(PLAYLIST_KEY)
            ?: throw IllegalArgumentException("Playlist data is required")
        tracks = arguments?.getParcelableArrayList(TRACKS_KEY) ?: emptyList()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet_share, container, false)
        setupUI(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_new_playlist)

    }

    override fun onStart() {
        super.onStart()


        dialog?.window?.let { dialog ->
            // Устанавливаем высоту окна шторки (50% экрана)
            val bouttoomSheetShare =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

            val behavior = BottomSheetBehavior.from(bouttoomSheetShare!!)
            bouttoomSheetShare.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.46).toInt()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.isHideable = true
            behavior.isFitToContents = false


        }
    }

    private fun setupUI(view: View) {


        // Устанавливаем данные плейлиста в `playlistPreview1`
        val coverView: ImageView = view.findViewById(R.id.playlistCoverBottomSheet)
        val nameView: TextView = view.findViewById(R.id.playlistNameBottomSheet)
        val trackCountView: TextView = view.findViewById(R.id.playlistTrackCount)

        nameView.text = playlist.name

        // Устанавливаем количество треков через plurals
        val trackCountText = resources.getQuantityString(
            R.plurals.track_count,
            playlist.trackCount,
            playlist.trackCount
        )
        trackCountView.text = trackCountText

        if (!playlist.coverPath.isNullOrEmpty() && File(playlist.coverPath).exists()) {
            Glide.with(coverView.context)
                .load(File(playlist.coverPath))
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(coverView)
        } else {
            coverView.setImageResource(R.drawable.placeholder)
        }

        // Кнопка "Поделиться"
        view.findViewById<TextView>(R.id.sheetShare).setOnClickListener {
            if (tracks.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.empty_playlist_message),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val message = createShareMessage(playlist, tracks)
                sharePlaylist(message)
            }
            dismiss()
        }
        // Кнопка "Редактировать"
        view.findViewById<TextView>(R.id.sheetRedactor).setOnClickListener {
                openEditPlaylistFragment()


        }

         // Кнопка "Удалить"
        view.findViewById<TextView>(R.id.sheetDelete).setOnClickListener {
            showDeletePlaylistDialog()
            dismiss()
        }
    }

    private fun createShareMessage(playlist: PlaylistModel, tracks: List<Track>): String {
        val message = StringBuilder()
        message.append("${playlist.name}\n")
        message.append("${playlist.description}\n\n")
        message.append(
            "[${tracks.size}] ${
                resources.getQuantityString(
                    R.plurals.track_count,
                    tracks.size,
                    tracks.size
                )
            }\n\n"
        )

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
        startActivity(Intent.createChooser(intent, getString(R.string.share_playlist_title)))
    }


    private fun showDeletePlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_playlist_title))
            .setMessage(getString(R.string.delete_playlist_message))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                deletePlaylist()
                dismiss()
                dialog.dismiss()

            }
            .show()
        dismiss()
    }

    private fun deletePlaylist() {
        parentFragmentCallback.deleteCurrentPlaylist(playlist)

        dismiss()


    }

    private fun openEditPlaylistFragment() {
        Toast.makeText(requireContext(), R.string.edit_playlist, Toast.LENGTH_SHORT)
        dismiss()
        val bundle = Bundle().apply {
            putParcelable("PLAYLIST_KEY", playlist)
        }
        findNavController().navigate(R.id.action_playlistDetailsFragment_to_editPlaylistFragment2,bundle)




    }


}
