package com.example.playlistmaker.presentation.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentEditPlaylistBinding
import com.example.playlistmaker.domain.model.PlaylistModel
import com.example.playlistmaker.presentation.ui.fragment.PlaylistDetailsFragment
import com.example.playlistmaker.presentation.viewmodel.EditPlaylistViewModel
import com.example.playlistmaker.presentation.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class EditPlaylistFragment : Fragment() {

    private val editPlaylistViewModel: EditPlaylistViewModel by viewModel()


    private var _binding: FragmentEditPlaylistBinding? = null
    private val binding get() = _binding!!

    private var coverUri: Uri? = null
    private var playlist: PlaylistModel? = null
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val PLAYLIST_KEY = "PLAYLIST_KEY"

        fun newInstance(
            playlist: PlaylistModel,
             coverPath: String?,
         ): EditPlaylistFragment {
            return EditPlaylistFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("playlist", playlist)
                    putString("coverPath", coverPath)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val playlist = arguments?.getParcelable<PlaylistModel>("PLAYLIST_KEY")
        if (playlist != null) {
            editPlaylistViewModel.setPlaylistData(playlist)
            updateUI(playlist)
        } else {
            Toast.makeText(requireContext(), "Ошибка передачи данных плейлиста", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }


        // Устанавливаем данные, если они есть
        playlist?.let {
            editPlaylistViewModel.setPlaylistData(it)
            updateUI(it)
        }

        setupObservers()

        // Логика при нажатии на кнопку "Назад"
        binding.toolbarNewPlaylist2.setNavigationOnClickListener {
            navigateBackToPlaylistDetails(isSaved = false)

        }


        // Логика при нажатии на кнопку "Сохранить"
        binding.createButton2.setOnClickListener {
            if (binding.createButton2.isEnabled) {
                editPlaylistViewModel.onCreateButtonClicked() // Переписываем текущий плейлист
                Toast.makeText(requireContext(), "Изменения сохранены", Toast.LENGTH_SHORT).show()
                navigateBackToPlaylistDetails(true) // Возврат к предыдущему экрану
            }
        }

         requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateBackToPlaylistDetails(isSaved = false)
                }
            })

        // Подписка на изменения текста в поле названия
        binding.playlistNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editPlaylistViewModel.onPlaylistNameChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.playlistDescriptionInput2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editPlaylistViewModel.onPlaylistDescriptionChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Логика при нажатии на изображение для выбора новой обложки
        binding.imagePlayList2.setOnClickListener {
            pickImageFromGallery()
        }
    }

    // TODO: new
    private fun setupObservers() {
        editPlaylistViewModel.isCreateButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.createButton2.isEnabled = isEnabled
            val background = if (isEnabled) {
                R.drawable.button_background_selector
            } else {
                R.drawable.button_background_selector
            }
            binding.createButton2.setBackgroundResource(background)
        }

        editPlaylistViewModel.coverImagePath.observe(viewLifecycleOwner) { coverPath ->
            coverPath?.let {
                Glide.with(this).load(it).into(binding.imagePlayList2)
            }
        }
    }

    // TODO: new fun
    private fun updateUI(playlist: PlaylistModel) {
        binding.playlistNameInput.setText(playlist.name)
        binding.playlistDescriptionInput2.setText(playlist.description)

        val coverPath = playlist.coverPath
        if (!coverPath.isNullOrEmpty() && File(coverPath).exists()) {
            // Если обложка есть, загружаем её
            Glide.with(requireContext())
                .load(coverPath)
                .into(binding.imagePlayList2)
        } else {
            // Если обложки нет, устанавливаем заглушку
            Glide.with(requireContext())
                .load(R.drawable.placeholder) // Укажите ресурс вашей заглушки
                .into(binding.imagePlayList2)
        }
    }


    // Переход назад на экран с деталями плейлиста
    private fun navigateBackToPlaylistDetails(isSaved: Boolean) {
        if (isSaved) {
            val updatedPlaylist = playlist?.copy(
                name = binding.playlistNameInput.text.toString(),
                description = binding.playlistDescriptionInput2.text.toString(),
                coverPath = coverUri?.toString() ?: playlist?.coverPath
            )
            updatedPlaylist?.let {
                findNavController().previousBackStackEntry?.savedStateHandle?.set("PLAYLIST_KEY", updatedPlaylist)
                val playlistsViewModel: PlaylistsViewModel by activityViewModels()
                playlistsViewModel.loadPlaylists() // Обновляем список
            }
        }
        findNavController().popBackStack()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            coverUri = data?.data
            coverUri?.let { uri ->
                val coverPath = saveCoverImageToInternalStorage(uri)
                binding.imagePlayList2.setImageURI(uri) // Отображение изображения
                editPlaylistViewModel.setCoverImagePath(coverPath) // Передача в ViewModel
            }
        }
    }

    private fun saveCoverImageToInternalStorage(imageUri: Uri): String? {
        val imageFile = File(requireContext().filesDir, "covers/${System.currentTimeMillis()}.jpg")
        imageFile.parentFile?.mkdirs()
        requireContext().contentResolver.openInputStream(imageUri)?.use { inputStream ->
            FileOutputStream(imageFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return imageFile.absolutePath
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
