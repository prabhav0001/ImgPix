package com.deecode.walls.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.deecode.walls.ui.common.UiState
import com.deecode.walls.ui.viewmodel.AlbumViewModel

@OptIn(ExperimentalFoundationApi::class)
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
            val images = state.data
            val pagerState = rememberPagerState(
                initialPage = imageIndex.coerceIn(0, images.size - 1),
                pageCount = { images.size }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    key = { it }
                ) { page ->
                    var scale by remember { mutableFloatStateOf(1f) }
                    var offsetX by remember { mutableFloatStateOf(0f) }
                    var offsetY by remember { mutableFloatStateOf(0f) }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = images[page],
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale,
                                    translationX = offsetX,
                                    translationY = offsetY
                                )
                                .pointerInput(Unit) {
                                    detectTransformGestures { _, pan, zoom, _ ->
                                        scale = (scale * zoom).coerceIn(1f, 5f)

                                        if (scale > 1f) {
                                            offsetX += pan.x
                                            offsetY += pan.y
                                        } else {
                                            offsetX = 0f
                                            offsetY = 0f
                                        }
                                    }
                                },
                            contentScale = ContentScale.Fit
                        )
                    }

                    // Reset zoom when page changes
                    LaunchedEffect(page) {
                        scale = 1f
                        offsetX = 0f
                        offsetY = 0f
                    }
                }
            }
        }
        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                // Loading indicator
            }
        }
        is UiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                // Error view
            }
        }
        else -> {}
    }
}
