package com.wallpaper.app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wallpaper.app.data.model.Photo
import com.wallpaper.app.data.repository.WallpaperRepository
import kotlinx.coroutines.launch

class WallpaperViewModel(private val repository: WallpaperRepository) : ViewModel() {

    private val _wallpapers = MutableLiveData<List<Photo>>()
    val wallpapers: LiveData<List<Photo>> = _wallpapers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentPage = 1
    private var isLastPage = false
    private var currentQuery: String? = null

    init {
        loadCuratedWallpapers()
    }

    fun loadCuratedWallpapers() {
        currentQuery = null
        currentPage = 1
        isLastPage = false
        fetchWallpapers()
    }

    fun searchWallpapers(query: String) {
        if (query.isBlank()) {
            loadCuratedWallpapers()
            return
        }
        currentQuery = query
        currentPage = 1
        isLastPage = false
        fetchWallpapers()
    }

    fun loadMoreWallpapers() {
        if (!isLastPage && _isLoading.value != true) {
            currentPage++
            fetchWallpapers(append = true)
        }
    }

    private fun fetchWallpapers(append: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = if (currentQuery != null) {
                repository.searchWallpapers(currentQuery!!, currentPage)
            } else {
                repository.getCuratedWallpapers(currentPage)
            }

            result.fold(
                onSuccess = { photos ->
                    if (photos.isEmpty()) {
                        isLastPage = true
                    }
                    val currentList = if (append) _wallpapers.value.orEmpty() else emptyList()
                    _wallpapers.value = currentList + photos
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Unknown error occurred"
                }
            )

            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
