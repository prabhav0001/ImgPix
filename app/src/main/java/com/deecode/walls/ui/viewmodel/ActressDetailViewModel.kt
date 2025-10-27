package com.deecode.walls.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.deecode.walls.data.local.FavoriteActress
import com.deecode.walls.data.local.FavoriteImage
import com.deecode.walls.data.model.ActressDetail
import com.deecode.walls.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActressDetailViewModel(application: Application) : BaseViewModel(application) {

    private val _actressDetail = MutableStateFlow<UiState<ActressDetail>>(UiState.Idle)
    val actressDetail: StateFlow<UiState<ActressDetail>> = _actressDetail.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _favoriteImages = MutableStateFlow<Set<String>>(emptySet())
    val favoriteImages: StateFlow<Set<String>> = _favoriteImages.asStateFlow()

    fun loadActressDetail(actressId: String) {
        viewModelScope.launch {
            _actressDetail.value = UiState.Loading

            repository.getActressDetail(actressId).fold(
                onSuccess = { detail ->
                    _actressDetail.value = UiState.Success(detail)

                    // Load favorite status for all images (collect once for all)
                    launch {
                        repository.getAllFavoriteImages().collect { favoritesList ->
                            val favoriteUrls = favoritesList.map { it.imageUrl }.toSet()
                            _favoriteImages.value = favoriteUrls.intersect(detail.images.toSet())
                        }
                    }
                },
                onFailure = { error ->
                    _actressDetail.value = UiState.Error(
                        error.message ?: "Failed to load actress details"
                    )
                }
            )

            // Check if favorite actress
            repository.isFavoriteActress(actressId).collect { isFav ->
                _isFavorite.value = isFav
            }
        }
    }

    fun toggleFavorite(actressId: String, name: String, thumbnail: String?) {
        viewModelScope.launch {
            if (_isFavorite.value) {
                repository.removeFavoriteActress(actressId)
            } else {
                repository.addFavoriteActress(
                    FavoriteActress(
                        id = actressId,
                        name = name,
                        thumbnail = thumbnail
                    )
                )
            }
        }
    }

    fun toggleImageFavorite(imageUrl: String, actressName: String, actressId: String) {
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
