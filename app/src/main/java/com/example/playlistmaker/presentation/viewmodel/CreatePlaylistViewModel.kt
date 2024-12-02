package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import kotlinx.coroutines.launch

open class CreatePlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
//создать
val _isCreateButtonEnabled = MutableLiveData(false)
    val isCreateButtonEnabled: LiveData<Boolean> get() = _isCreateButtonEnabled
//название
val _isPlaylistNameEnabled = MutableLiveData(false)
    val isPlaylistNameEnabled: LiveData<Boolean> get() = _isPlaylistNameEnabled
//описание
val _isPlaylistDescriptionEnabled = MutableLiveData(false)
    val isPlaylistDescriptionEnabled: LiveData<Boolean> get() = _isPlaylistDescriptionEnabled

    private val _showDiscardDialog = MutableLiveData<Boolean>()
    val showDiscardDialog: LiveData<Boolean> get() = _showDiscardDialog

    val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    val _showToastMessage = MutableLiveData<String>()
    val showToastMessage: LiveData<String> get() = _showToastMessage


    // LiveData для отслеживания имени плейлиста
    val _playlistName = MutableLiveData<String>()
    val playlistNameCreate: LiveData<String> = _playlistName

    // LiveData для отслеживания состояния, пустое ли поле или нет
    private val _isPlaylistNameEmpty = MutableLiveData<Boolean>()
    val isPlaylistNameEmpty: LiveData<Boolean> = _isPlaylistNameEmpty

    // LiveData для отслеживания описания плейлиста
    val _playlistDescription = MutableLiveData<String?>()
    val playlistDescription: LiveData<String?> get() = _playlistDescription

    // LiveData для отслеживания пути к изображению обложки
    val _coverImagePath = MutableLiveData<String?>()
    val coverImagePath: LiveData<String?> get() = _coverImagePath


    var playlistName: String = ""
    var playlistDescriptionText: String? = null
    var coverPath: String? = null

    fun onPlaylistNameChanged(name: String) {
        playlistName = name
        _isPlaylistNameEnabled.value = name.isNotBlank() // TODO:
        _isPlaylistNameEmpty.value = name.isEmpty()
        validateInput()
    }



    fun onPlaylistDescriptionChanged(description: String?) {
        playlistDescriptionText = description
        _playlistDescription.value = description
        _isPlaylistDescriptionEnabled.value = !description.isNullOrBlank()
    }

    fun setCoverImagePath(path: String?) {
        coverPath = path
        _coverImagePath.value = path
    }

    fun validateInput() {
        _isCreateButtonEnabled.value = playlistName.isNotBlank()
//        _isCreateButtonEnabled.value = playlistName.isNotEmpty()
    }

    open fun onCreateButtonClicked() {
        if (_isCreateButtonEnabled.value == true) {
            viewModelScope.launch {
                playlistInteractor.createPlaylist(playlistName, playlistDescriptionText, coverPath)
                _showToastMessage.value = "Плейлист \"$playlistName\" создан"
                _navigateBack.value = true
            }
        }
    }

    open fun onBackButtonClicked() {
        if (playlistName.isNotBlank() || !playlistDescriptionText.isNullOrEmpty() || coverPath != null) {
            _showDiscardDialog.value = true
        } else {
            _navigateBack.value = true
        }
    }

    fun discardChanges() {
        _navigateBack.value = true
    }


}