package com.deecode.walls.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deecode.walls.ui.common.UiState
import com.deecode.walls.ui.components.ImageViewerPager
import com.deecode.walls.ui.viewmodel.AlbumViewModel

@Composable
fun AlbumImageViewerScreen(
    albumUrl: String,
    imageIndex: Int,
    viewModel: AlbumViewModel = viewModel()
) {
    // Load album photos
    LaunchedEffect(albumUrl) {
        viewModel.loadAlbumPhotos(albumUrl)
    }

    val uiState by viewModel.albumPhotos.collectAsState()

    when (val state = uiState) {
        is UiState.Success -> {
            ImageViewerPager(
                images = state.data,
                initialIndex = imageIndex,
                contentDescription = null
            )
        }
        else -> {
            // Loading or error state - just show black screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )
        }
    }
}
