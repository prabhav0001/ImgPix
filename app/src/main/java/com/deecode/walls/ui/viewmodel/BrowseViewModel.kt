package com.deecode.walls.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deecode.walls.data.local.WallsDatabase
import com.deecode.walls.data.model.Actress
import com.deecode.walls.data.remote.RetrofitInstance
import com.deecode.walls.data.repository.WallsRepository
import com.deecode.walls.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BrowseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WallsRepository

    init {
        val database = WallsDatabase.getDatabase(application)
        repository = WallsRepository(RetrofitInstance.api, database.favoriteDao())
    }

    private val _actresses = MutableStateFlow<UiState<List<Actress>>>(UiState.Idle)
    val actresses: StateFlow<UiState<List<Actress>>> = _actresses.asStateFlow()

    private val _selectedLetter = MutableStateFlow("A")
    val selectedLetter: StateFlow<String> = _selectedLetter.asStateFlow()

    fun loadActressesByLetter(letter: String) {
        viewModelScope.launch {
            _selectedLetter.value = letter
            _actresses.value = UiState.Loading

            repository.getActressesByLetter(letter.lowercase()).fold(
                onSuccess = { actressList ->
                    _actresses.value = UiState.Success(actressList)
                },
                onFailure = { error ->
                    _actresses.value = UiState.Error(
                        error.message ?: "Failed to load actresses"
                    )
                }
            )
        }
    }
}
