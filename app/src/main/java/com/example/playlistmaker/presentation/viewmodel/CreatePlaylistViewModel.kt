package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
//создать
    private val _isCreateButtonEnabled = MutableLiveData(false)
    val isCreateButtonEnabled: LiveData<Boolean> get() = _isCreateButtonEnabled
//название
    private val _isPlaylistNameEnabled = MutableLiveData(false)
    val isPlaylistNameEnabled: LiveData<Boolean> get() = _isPlaylistNameEnabled
//описание
    private val _isPlaylistDescriptionEnabled = MutableLiveData(false)
    val isPlaylistDescriptionEnabled: LiveData<Boolean> get() = _isPlaylistDescriptionEnabled

    private val _showDiscardDialog = MutableLiveData<Boolean>()
    val showDiscardDialog: LiveData<Boolean> get() = _showDiscardDialog

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    private val _showToastMessage = MutableLiveData<String>()
    val showToastMessage: LiveData<String> get() = _showToastMessage

    private var playlistName: String = ""
    private var playlistDescription: String? = null
    private var coverPath: String? = null

    fun onPlaylistNameChanged(name: String) {
        playlistName = name
        _isPlaylistNameEnabled.value = name.isNotBlank() // TODO:
        validateInput()
    }

    fun onPlaylistDescriptionChanged(description: String?) {
        playlistDescription = description
        _isPlaylistDescriptionEnabled.value = !description.isNullOrBlank()
    }

    fun setCoverImagePath(path: String?) {
        coverPath = path
    }

    private fun validateInput() {
        _isCreateButtonEnabled.value = playlistName.isNotBlank()
    }

    fun onCreateButtonClicked() {
        if (_isCreateButtonEnabled.value == true) {
            viewModelScope.launch {
                playlistInteractor.createPlaylist(playlistName, playlistDescription, coverPath)
                _showToastMessage.value = "Плейлист \"$playlistName\" создан"
                _navigateBack.value = true
            }
        }
    }

    fun onBackButtonClicked() {
        if (playlistName.isNotBlank() || !playlistDescription.isNullOrEmpty() || coverPath != null) {
            _showDiscardDialog.value = true
        } else {
            _navigateBack.value = true
        }
    }

    fun discardChanges() {
        _navigateBack.value = true
    }

    fun cancelDiscardDialog() {
        _showDiscardDialog.value = false
    }
}