package com.example.playlistmaker.presentation.ui.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.presentation.ui.activity.MainActivity
import com.example.playlistmaker.presentation.viewmodel.CreatePlaylistViewModel
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.io.FileOutputStream
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateFragment : Fragment() {
    // Binding для доступа к элементам интерфейса
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    // ViewModel для управления состоянием UI
    private val viewModel: CreatePlaylistViewModel by viewModel()

    // Переменная для хранения URI выбранного изображения
    private var coverUri: Uri? = null

    // Константы для обработки выбора изображения
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val STORAGE_PERMISSION_REQUEST_CODE = 100
    }

    // Создание макета фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Установка обработчиков и наблюдателей
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Скрываем нижнюю панель навигации
        (activity as? MainActivity)?.hideBottomNavigation()

        setupObservers() // Подключаем наблюдателей
        setupListeners() // Устанавливаем слушатели для элементов интерфейса
    }

    // Подключение наблюдателей к ViewModel
    private fun setupObservers() {
        // Наблюдение за состоянием кнопки "Создать"
        viewModel.isCreateButtonEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.createButton.isEnabled = isEnabled
            val background = if (isEnabled) {
                R.drawable.button_background_selector // Включенная кнопка
            } else {
                R.drawable.button_background_selector // Отключенная кнопка
            }
            binding.createButton.setBackgroundResource(background)
        }

        // Наблюдение за показом сообщений Toast
        viewModel.showToastMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        // Наблюдение за переходом назад
        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigateBack ->
            if (shouldNavigateBack) {
                requireActivity().onBackPressed()
            }
        }

        // Наблюдение за показом диалога подтверждения
        viewModel.showDiscardDialog.observe(viewLifecycleOwner) { shouldShow ->
            if (shouldShow) {
                showExitConfirmationDialog()
            }
        }

        // Наблюдение за изменениями в текстовых полях
        viewModel.isPlaylistNameEnabled.observe(viewLifecycleOwner) { isNonEmpty ->
            updateFieldBorder(binding.playlistNameInputLayout, isNonEmpty)
        }
        viewModel.isPlaylistDescriptionEnabled.observe(viewLifecycleOwner) { isNonEmpty ->
            updateFieldBorder(binding.playlistDescriptionInputLayout, isNonEmpty)
        }
    }

    // Установка слушателей для элементов интерфейса
    private fun setupListeners() {
        // Слушатель изменений текста в поле имени плейлиста
        binding.playlistNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPlaylistNameChanged(s.toString()) // Обновление имени в ViewModel
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Слушатель изменений текста в поле описания плейлиста
        binding.playlistDescriptionInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onPlaylistDescriptionChanged(s.toString()) // Обновление описания в ViewModel
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Слушатель кнопки "Создать плейлист"
        binding.createButton.setOnClickListener {
            if (binding.createButton.isEnabled) {
                viewModel.onCreateButtonClicked()
            }
        }

        // Слушатель кнопки "Назад"
        binding.buttonCreatePlaylist.setOnClickListener {
            viewModel.onBackButtonClicked()
        }

        // Слушатель контейнера для выбора обложки
        binding.coverContainer.setOnClickListener {
            pickImageFromGallery()
        }
    }

    // Открытие галереи для выбора изображения
    private fun pickImageFromGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Обработка результата выбора изображения
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            coverUri = data?.data // Получение URI выбранного изображения
            coverUri?.let { uri ->
                binding.coverImageView.setImageURI(uri) // Установка изображения
                binding.coverImageView.visibility = View.VISIBLE
                binding.addCoverIcon.visibility = View.GONE

                val coverPath = saveCoverImageToInternalStorage(uri) // Сохранение во внутреннее хранилище
                viewModel.setCoverImagePath(coverPath) // Передача в ViewModel
            }
        }
    }

    // Обработка результата запроса разрешений
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            Toast.makeText(requireContext(), "Permission required to access media", Toast.LENGTH_SHORT).show()
        }
    }

    // Сохранение изображения во внутреннее хранилище
    private fun saveCoverImageToInternalStorage(imageUri: Uri): String? {
        val imageFile = File(requireContext().filesDir, "covers/${System.currentTimeMillis()}.jpg")
        imageFile.parentFile?.mkdirs() // Создание директорий при необходимости
        requireContext().contentResolver.openInputStream(imageUri)?.use { inputStream ->
            FileOutputStream(imageFile).use { outputStream ->
                inputStream.copyTo(outputStream) // Копирование данных изображения
            }
        }
        return imageFile.absolutePath // Возврат пути к сохраненному файлу
    }

    // Показ диалога подтверждения выхода
    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setPositiveButton("Завершить") { _, _ -> viewModel.discardChanges() }
            .setNegativeButton("Отмена", null)
            .show()
    }

    // Обновление рамки текстового поля
    private fun updateFieldBorder(layout: TextInputLayout, isNonEmpty: Boolean) {
        val background = if (isNonEmpty) {
            R.drawable.button_background_selector // Фокус с введенным текстом
        } else {
            R.drawable.rounded_border_focused // Пустое поле
        }
        layout.setBackgroundResource(background)
    }

    // Освобождение ресурсов
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as? MainActivity)?.showBottomNavigation()
    }
}
