package com.deecode.walls.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deecode.walls.data.local.FavoriteActress
import com.deecode.walls.data.local.WallsDatabase
import com.deecode.walls.data.model.ActressDetail
import com.deecode.walls.data.remote.RetrofitInstance
import com.deecode.walls.data.repository.WallsRepository
import com.deecode.walls.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActressDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WallsRepository

    init {
        val database = WallsDatabase.getDatabase(application)
        repository = WallsRepository(RetrofitInstance.api, database.favoriteDao())
    }

    private val _actressDetail = MutableStateFlow<UiState<ActressDetail>>(UiState.Idle)
    val actressDetail: StateFlow<UiState<ActressDetail>> = _actressDetail.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun loadActressDetail(actressId: String) {
        viewModelScope.launch {
            _actressDetail.value = UiState.Loading

            repository.getActressDetail(actressId).fold(
                onSuccess = { detail ->
                    _actressDetail.value = UiState.Success(detail)
                },
                onFailure = { error ->
                    _actressDetail.value = UiState.Error(
                        error.message ?: "Failed to load actress details"
                    )
                }
            )

            // Check if favorite
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

}
