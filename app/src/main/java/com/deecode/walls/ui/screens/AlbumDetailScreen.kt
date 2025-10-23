package com.deecode.walls.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deecode.walls.ui.common.UiState
import com.deecode.walls.ui.components.EmptyView
import com.deecode.walls.ui.components.ErrorView
import com.deecode.walls.ui.components.ImageCard
import com.deecode.walls.ui.components.LoadingView
import com.deecode.walls.ui.viewmodel.AlbumViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumUrl: String,
    albumName: String,
    onBackClick: () -> Unit,
    onImageClick: (String, Int) -> Unit,
    viewModel: AlbumViewModel = viewModel()
) {
    val uiState by viewModel.albumPhotos.collectAsState()

    LaunchedEffect(albumUrl) {
        viewModel.loadAlbumPhotos(albumUrl)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        albumName,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.height(56.dp)
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is UiState.Loading -> {
                LoadingView(modifier = Modifier.padding(paddingValues))
            }

            is UiState.Success -> {
                val photos = state.data

                if (photos.isEmpty()) {
                    EmptyView(
                        message = "No photos in this album",
                        modifier = Modifier.padding(paddingValues)
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = paddingValues.calculateTopPadding() + 16.dp,
                            bottom = paddingValues.calculateBottomPadding() + 16.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(photos) { photoUrl ->
                            val photoIndex = photos.indexOf(photoUrl)
                            ImageCard(
                                imageUrl = photoUrl,
                                onClick = { onImageClick(albumUrl, photoIndex) }
                            )
                        }
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
