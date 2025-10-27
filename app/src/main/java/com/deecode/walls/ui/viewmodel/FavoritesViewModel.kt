package com.deecode.walls.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.deecode.walls.data.local.FavoriteActress
import com.deecode.walls.data.local.FavoriteImage
import com.deecode.walls.data.local.WallsDatabase
import com.deecode.walls.data.remote.RetrofitInstance
import com.deecode.walls.data.repository.WallsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WallsRepository

    init {
        val database = WallsDatabase.getDatabase(application)
        repository = WallsRepository(RetrofitInstance.api, database.favoriteDao())
    }

    private val _favoriteActresses = MutableStateFlow<List<FavoriteActress>>(emptyList())
    val favoriteActresses: StateFlow<List<FavoriteActress>> = _favoriteActresses.asStateFlow()

    private val _favoriteImages = MutableStateFlow<List<FavoriteImage>>(emptyList())
    val favoriteImages: StateFlow<List<FavoriteImage>> = _favoriteImages.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0 = Actresses, 1 = Images
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    init {
        loadFavorites()
        fixNullThumbnails()
    }

    private fun fixNullThumbnails() {
        viewModelScope.launch {
            repository.getAllFavoriteActresses().collect { actresses ->
                actresses.forEach { actress ->
                    if (actress.thumbnail.isNullOrEmpty()) {
                        // Fetch actress details and update thumbnail
                        repository.getActressDetail(actress.id).fold(
                            onSuccess = { detail ->
                                val thumbnail = detail.images.firstOrNull()
                                    ?: detail.albums.firstOrNull()?.thumbnail

                                if (thumbnail != null) {
                                    repository.addFavoriteActress(
                                        actress.copy(thumbnail = thumbnail)
                                    )
                                }
                            },
                            onFailure = { /* Silently fail */ }
                        )
                    }
                }
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getAllFavoriteActresses().collect { actresses ->
                _favoriteActresses.value = actresses
            }
        }

        viewModelScope.launch {
            repository.getAllFavoriteImages().collect { images ->
                _favoriteImages.value = images
            }
        }
    }

    fun setSelectedTab(index: Int) {
        _selectedTab.value = index
    }

}
