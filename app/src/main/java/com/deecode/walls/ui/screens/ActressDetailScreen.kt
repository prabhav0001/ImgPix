package com.deecode.walls.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deecode.walls.ui.common.UiState
import com.deecode.walls.ui.components.AlbumCard
import com.deecode.walls.ui.components.ErrorView
import com.deecode.walls.ui.components.ImageCard
import com.deecode.walls.ui.components.LoadingView
import com.deecode.walls.ui.components.twoColumnGridSection
import com.deecode.walls.ui.viewmodel.ActressDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActressDetailScreen(
    actressId: String,
    onBackClick: () -> Unit,
    onAlbumClick: (String, String) -> Unit,
    onImageClick: (String, Int) -> Unit,
    viewModel: ActressDetailViewModel = viewModel()
) {
    val uiState by viewModel.actressDetail.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val favoriteImages by viewModel.favoriteImages.collectAsState()

    LaunchedEffect(actressId) {
        viewModel.loadActressDetail(actressId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    when (val state = uiState) {
                        is UiState.Success -> {
                            Text(
                                text = state.data.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        else -> {
                            Text(
                                "Profile Details",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    when (val state = uiState) {
                        is UiState.Success -> {
                            IconButton(
                                onClick = {
                                    // Use first image, or first album thumbnail as fallback
                                    val thumbnail = state.data.images.firstOrNull()
                                        ?: state.data.albums.firstOrNull()?.thumbnail

                                    viewModel.toggleFavorite(
                                        state.data.id,
                                        state.data.name,
                                        thumbnail
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite
                                    else Icons.Default.FavoriteBorder,
                                    contentDescription = "Toggle Favorite",
                                    tint = if (isFavorite) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        else -> {}
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is UiState.Loading -> {
                LoadingView(modifier = Modifier.padding(paddingValues))
            }

            is UiState.Success -> {
                val actress = state.data

                // Show images in 2-column grid and albums below
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Images section
                    twoColumnGridSection(
                        items = actress.images,
                        title = "Images"
                    ) { imageUrl, modifier ->
                        val imageIndex = actress.images.indexOf(imageUrl)
                        ImageCard(
                            imageUrl = imageUrl,
                            onClick = { onImageClick(actress.id, imageIndex) },
                            modifier = modifier,
                            isFavorite = favoriteImages.contains(imageUrl),
                            onFavoriteClick = {
                                viewModel.toggleImageFavorite(
                                    imageUrl = imageUrl,
                                    actressName = actress.name,
                                    actressId = actress.id
                                )
                            }
                        )
                    }

                    // Albums section
                    twoColumnGridSection(
                        items = actress.albums,
                        title = "Albums"
                    ) { album, modifier ->
                        AlbumCard(
                            name = album.name,
                            thumbnail = album.thumbnail,
                            onClick = { onAlbumClick(album.url, album.name) },
                            modifier = modifier
                        )
                    }
                }
            }

            is UiState.Error -> {
                ErrorView(
                    message = state.message,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {}
        }
    }
}
