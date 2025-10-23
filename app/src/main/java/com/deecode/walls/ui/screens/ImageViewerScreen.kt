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
import com.deecode.walls.ui.viewmodel.ActressDetailViewModel

@Composable
fun ImageViewerScreen(
    actressId: String,
    imageIndex: Int,
    viewModel: ActressDetailViewModel = viewModel()
) {
    // Load actress detail to get images
    LaunchedEffect(actressId) {
        viewModel.loadActressDetail(actressId)
    }

    val uiState by viewModel.actressDetail.collectAsState()

    when (val state = uiState) {
        is UiState.Success -> {
            ImageViewerPager(
                images = state.data.images,
                initialIndex = imageIndex,
                contentDescription = state.data.name
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
