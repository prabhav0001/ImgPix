package com.deecode.walls.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

/**
 * Reusable full-screen image viewer with swipe and zoom
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageViewerPager(
    images: List<String>,
    initialIndex: Int = 0,
    contentDescription: String? = null
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex.coerceIn(0, images.size - 1),
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
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            // Only add gesture detection when zoomed
                            if (scale > 1f) {
                                Modifier.pointerInput(Unit) {
                                    detectTransformGestures { _, pan, zoom, _ ->
                                        scale = (scale * zoom).coerceIn(1f, 5f)

                                        if (scale > 1f) {
                                            val maxX = (size.width * (scale - 1)) / 2
                                            val maxY = (size.height * (scale - 1)) / 2

                                            offsetX = (offsetX + pan.x).coerceIn(-maxX, maxX)
                                            offsetY = (offsetY + pan.y).coerceIn(-maxY, maxY)
                                        } else {
                                            offsetX = 0f
                                            offsetY = 0f
                                        }
                                    }
                                }
                            } else {
                                // When not zoomed, only detect pinch to zoom in
                                Modifier.pointerInput(Unit) {
                                    detectTransformGestures { _, _, zoom, _ ->
                                        if (zoom != 1f) {
                                            scale = (scale * zoom).coerceIn(1f, 5f)
                                        }
                                    }
                                }
                            }
                        )
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        ),
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
