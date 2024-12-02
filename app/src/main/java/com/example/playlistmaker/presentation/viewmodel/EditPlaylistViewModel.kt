package com.example.playlistmaker.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactor.PlaylistInteractor
import com.example.playlistmaker.domain.model.PlaylistModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : CreatePlaylistViewModel(playlistInteractor) {

    private val _playlist = MutableLiveData<PlaylistModel>()
    val playlist: LiveData<PlaylistModel> get() = _playlist

    fun setPlaylistData(playlistModel: PlaylistModel) {
        _playlist.value = playlistModel
        playlistName = playlistModel.name
        playlistDescriptionText = playlistModel.description
        coverPath = playlistModel.coverPath

        // Обновляем данные в UI
        _playlistName.value = playlistModel.name
        _playlistDescription.value = playlistModel.description
        _coverImagePath.value = playlistModel.coverPath

        // Проверяем, чтобы кнопка "Сохранить" была активна, если название не пустое
        validateInput()
    }

    override fun onCreateButtonClicked() {
        if (_isCreateButtonEnabled.value == true) {
            val currentPlaylist = _playlist.value

            if (currentPlaylist != null) {
                viewModelScope.launch {
                    // Обновляем плейлист в базе данных через интерактор
                    playlistInteractor.updatePlaylist(
                        playlistId = currentPlaylist.id,
                        name = playlistName,
                        description = playlistDescriptionText,
                        coverPath = coverPath
                    )
                    _showToastMessage.value = "Плейлист \"$playlistName\" обновлён"
                    _navigateBack.value = true
                }
            }
        }
    }

     override fun onBackButtonClicked() {
        _navigateBack.value = true
    }



    fun updatePlaylist(playlist: PlaylistModel) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(
                playlistId = playlist.id,
                name = playlist.name,
                description = playlist.description,
                coverPath = playlist.coverPath
            )
            _playlist.postValue(playlist)
        }

    }
}
