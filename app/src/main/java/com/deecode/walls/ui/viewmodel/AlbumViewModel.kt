package com.deecode.walls.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deecode.walls.data.local.WallsDatabase
import com.deecode.walls.data.remote.RetrofitInstance
import com.deecode.walls.data.repository.WallsRepository
import com.deecode.walls.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WallsRepository

    init {
        val database = WallsDatabase.getDatabase(application)
        repository = WallsRepository(RetrofitInstance.api, database.favoriteDao())
    }

    private val _albumPhotos = MutableStateFlow<UiState<List<String>>>(UiState.Idle)
    val albumPhotos: StateFlow<UiState<List<String>>> = _albumPhotos.asStateFlow()

    fun loadAlbumPhotos(albumUrl: String) {
        viewModelScope.launch {
            _albumPhotos.value = UiState.Loading

            repository.getAlbumPhotos(albumUrl).fold(
                onSuccess = { photos ->
                    _albumPhotos.value = UiState.Success(photos)
                },
                onFailure = { error ->
                    _albumPhotos.value = UiState.Error(
                        error.message ?: "Failed to load album photos"
                    )
                }
            )
        }
    }
}
