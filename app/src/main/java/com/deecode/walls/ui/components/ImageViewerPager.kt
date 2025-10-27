package com.deecode.walls.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/**
 * Full-screen image viewer with horizontal swipe navigation and zoom capabilities
 *
 * Features:
 * - Swipe left/right to navigate between images
 * - Double tap to zoom in/out (2.5x)
 * - Pinch to zoom (1x - 5x) when zoomed in
 * - Pan/drag to move around when zoomed
 * - Image counter badge showing current position
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
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    scale = if (scale > 1f) 1f else 2.5f
                                    offsetX = 0f
                                    offsetY = 0f
                                }
                            )
                        }
                        // Only enable zoom/pan gestures when already zoomed in
                        // This allows HorizontalPager swipe to work normally when not zoomed
                        .then(
                            if (scale > 1f) {
                                Modifier.pointerInput(scale) {
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
                                Modifier
                            }
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

        // Image counter indicator
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 16.dp),
            color = Color.Black.copy(alpha = 0.6f),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "${pagerState.currentPage + 1} / ${images.size}",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
