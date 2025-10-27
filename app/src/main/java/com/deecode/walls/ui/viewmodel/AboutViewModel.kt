package com.deecode.walls.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.deecode.walls.data.model.ApiResponse
import com.deecode.walls.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AboutViewModel(application: Application) : BaseViewModel(application) {

    private val _apiInfoState = MutableStateFlow<UiState<ApiResponse>>(UiState.Idle)
    val apiInfoState: StateFlow<UiState<ApiResponse>> = _apiInfoState.asStateFlow()

    fun loadApiInfo() {
        _apiInfoState.value = UiState.Loading
        viewModelScope.launch {
            repository.getApiInfo().fold(
                onSuccess = { response ->
                    _apiInfoState.value = UiState.Success(response)
                },
                onFailure = { error ->
                    _apiInfoState.value = UiState.Error(
                        error.message ?: "Failed to load API info"
                    )
                }
            )
        }
    }
}
