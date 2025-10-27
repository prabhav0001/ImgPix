package com.deecode.walls.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.deecode.walls.data.local.FavoriteImage
import com.deecode.walls.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumViewModel(application: Application) : BaseViewModel(application) {

    private val _albumPhotos = MutableStateFlow<UiState<List<String>>>(UiState.Idle)
    val albumPhotos: StateFlow<UiState<List<String>>> = _albumPhotos.asStateFlow()

    private val _favoriteImages = MutableStateFlow<Set<String>>(emptySet())
    val favoriteImages: StateFlow<Set<String>> = _favoriteImages.asStateFlow()

    fun loadAlbumPhotos(albumUrl: String) {
        viewModelScope.launch {
            _albumPhotos.value = UiState.Loading

            repository.getAlbumPhotos(albumUrl).fold(
                onSuccess = { photos ->
                    _albumPhotos.value = UiState.Success(photos)

                    // Load favorite status for all images (collect once for each)
                    launch {
                        repository.getAllFavoriteImages().collect { favoritesList ->
                            val favoriteUrls = favoritesList.map { it.imageUrl }.toSet()
                            _favoriteImages.value = favoriteUrls.intersect(photos.toSet())
                        }
                    }
                },
                onFailure = { error ->
                    _albumPhotos.value = UiState.Error(
                        error.message ?: "Failed to load album photos"
                    )
                }
            )
        }
    }

    fun toggleFavorite(imageUrl: String, actressName: String, actressId: String) {
        viewModelScope.launch {
            if (_favoriteImages.value.contains(imageUrl)) {
                repository.removeFavoriteImage(imageUrl)
            } else {
                repository.addFavoriteImage(
                    FavoriteImage(
                        imageUrl = imageUrl,
                        actressId = actressId,
                        actressName = actressName
                    )
                )
            }
        }
    }
}
