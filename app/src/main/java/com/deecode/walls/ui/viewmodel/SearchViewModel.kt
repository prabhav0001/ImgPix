package com.deecode.walls.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.deecode.walls.data.model.Actress
import com.deecode.walls.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : BaseViewModel(application) {

    private val _searchResults = MutableStateFlow<UiState<List<Actress>>>(UiState.Idle)
    val searchResults: StateFlow<UiState<List<Actress>>> = _searchResults.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        if (query.length < 2) {
            _searchResults.value = UiState.Idle
            return
        }

        performSearch(query)
    }

    fun performSearch(query: String) {
        if (query.length < 2) return

        viewModelScope.launch {
            _searchResults.value = UiState.Loading

            repository.searchActresses(query, 50).fold(
                onSuccess = { actresses ->
                    _searchResults.value = UiState.Success(actresses)
                },
                onFailure = { error ->
                    _searchResults.value = UiState.Error(
                        error.message ?: "Search failed"
                    )
                }
            )
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = UiState.Idle
    }
}
