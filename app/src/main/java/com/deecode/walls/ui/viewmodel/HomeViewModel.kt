package com.deecode.walls.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.deecode.walls.data.model.Actress
import com.deecode.walls.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseViewModel(application) {

    private val _latestGalleries = MutableStateFlow<UiState<List<Actress>>>(UiState.Idle)
    val latestGalleries: StateFlow<UiState<List<Actress>>> = _latestGalleries.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun loadLatestGalleries(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if (isRefresh) {
                _isRefreshing.value = true
            } else {
                _latestGalleries.value = UiState.Loading
            }

            repository.getLatestGalleries().fold(
                onSuccess = { actresses ->
                    _latestGalleries.value = UiState.Success(actresses)
                },
                onFailure = { error ->
                    _latestGalleries.value = UiState.Error(
                        error.message ?: "Failed to load galleries"
                    )
                }
            )

            if (isRefresh) {
                _isRefreshing.value = false
            }
        }
    }
}
