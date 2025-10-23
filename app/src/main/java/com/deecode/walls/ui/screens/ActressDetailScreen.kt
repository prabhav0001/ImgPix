package com.deecode.walls.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.deecode.walls.ui.common.UiState
import com.deecode.walls.ui.components.AlbumCard
import com.deecode.walls.ui.components.ErrorView
import com.deecode.walls.ui.components.LoadingView
import com.deecode.walls.ui.viewmodel.ActressDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActressDetailScreen(
    actressId: String,
    onBackClick: () -> Unit,
    onAlbumClick: (String, String) -> Unit,
    onImageClick: (String, String) -> Unit,
    viewModel: ActressDetailViewModel = viewModel()
) {
    val uiState by viewModel.actressDetail.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(actressId) {
        viewModel.loadActressDetail(actressId)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Profile Details") },
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
                                    viewModel.toggleFavorite(
                                        state.data.id,
                                        state.data.name,
                                        state.data.images.firstOrNull()
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
                colors = TopAppBarDefaults.topAppBarColors(
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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header info
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = actress.name,
                                    style = MaterialTheme.typography.headlineMedium
                                )

                                if (actress.profession != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = actress.profession,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (actress.nationality != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Nationality: ${actress.nationality}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                if (actress.bio != null && actress.bio.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = actress.bio,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    // Images section
                    if (actress.images.isNotEmpty()) {
                        item {
                            Text(
                                text = "Images (${actress.images.size})",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        items(actress.images) { imageUrl ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                onClick = { onImageClick(imageUrl, actress.name) }
                            ) {
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = actress.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(400.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    // Albums section
                    if (actress.albums.isNotEmpty()) {
                        item {
                            Text(
                                text = "Albums (${actress.albums.size})",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        items(actress.albums) { album ->
                            AlbumCard(
                                name = album.name,
                                thumbnail = album.thumbnail,
                                onClick = { onAlbumClick(album.url, album.name) }
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
