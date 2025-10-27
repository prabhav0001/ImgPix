package com.deecode.walls.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deecode.walls.ui.components.ImageViewerPager
import com.deecode.walls.ui.viewmodel.FavoritesViewModel

@Composable
fun FavoriteImageViewerScreen(
    imageIndex: Int,
    viewModel: FavoritesViewModel = viewModel()
) {
    val favoriteImages by viewModel.favoriteImages.collectAsState()

    if (favoriteImages.isNotEmpty()) {
        val imageUrls = favoriteImages.map { it.imageUrl }
        ImageViewerPager(
            images = imageUrls,
            initialIndex = imageIndex,
            contentDescription = "Favorite Images"
        )
    } else {
        // Loading or empty state - just show black screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
    }
}
