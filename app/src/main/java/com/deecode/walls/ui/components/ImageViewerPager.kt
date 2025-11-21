package com.deecode.walls.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.deecode.walls.R
import com.deecode.walls.util.ImageDownloader
import kotlinx.coroutines.launch

/**
 * Full-screen image viewer with swipe navigation and zoom
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageViewerPager(
    images: List<String>,
    initialIndex: Int = 0,
    contentDescription: String? = null
) {
    val context = LocalContext.current
    val imageDownloader = remember { ImageDownloader(context) }
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
            val scope = rememberCoroutineScope()
            val scale = remember { Animatable(1f) }
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
                            scaleX = scale.value,
                            scaleY = scale.value,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = { _ ->
                                    scope.launch {
                                        // Cycle through zoom levels: 1x -> 2x -> 3x -> 1x
                                        val targetScale = when {
                                            scale.value < 1.5f -> 2f
                                            scale.value < 2.5f -> 3f
                                            else -> 1f
                                        }

                                        // Animate zoom with spring effect
                                        scale.animateTo(
                                            targetValue = targetScale,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )

                                        // Reset position when zooming out to 1x
                                        if (targetScale == 1f) {
                                            offsetX = 0f
                                            offsetY = 0f
                                        }
                                    }
                                }
                            )
                        }
                        // Smooth pinch-to-zoom and pan gestures
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown(requireUnconsumed = false)
                                do {
                                    val event = awaitPointerEvent()
                                    val zoom = event.calculateZoom()
                                    val pan = event.calculatePan()

                                    if (zoom != 1f) {
                                        scope.launch {
                                            val newScale = (scale.value * zoom).coerceIn(1f, 5f)
                                            scale.snapTo(newScale)
                                        }
                                    }

                                    if (scale.value > 1f) {
                                        val maxX = (size.width * (scale.value - 1)) / 2
                                        val maxY = (size.height * (scale.value - 1)) / 2
                                        offsetX = (offsetX + pan.x).coerceIn(-maxX, maxX)
                                        offsetY = (offsetY + pan.y).coerceIn(-maxY, maxY)
                                    } else {
                                        offsetX = 0f
                                        offsetY = 0f
                                    }
                                } while (event.changes.any { it.pressed })
                            }
                        },
                    contentScale = ContentScale.Fit
                )
            }

            // Reset zoom when page changes
            LaunchedEffect(page) {
                scale.snapTo(1f)
                offsetX = 0f
                offsetY = 0f
            }
        }

        // Top Bar Controls
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize() // Fill size to allow alignment of children
        ) {
            // Image counter indicator
            Surface(
                modifier = Modifier.align(Alignment.TopCenter),
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

            // Download Button
            Surface(
                modifier = Modifier.align(Alignment.TopEnd),
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(50),
                onClick = {
                    val currentImageUrl = images[pagerState.currentPage]
                    imageDownloader.downloadImage(currentImageUrl)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = stringResource(R.string.download),
                    tint = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
