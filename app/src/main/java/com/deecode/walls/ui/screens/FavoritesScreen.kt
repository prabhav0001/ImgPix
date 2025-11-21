package com.deecode.walls.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deecode.walls.R
import com.deecode.walls.data.model.Actress
import com.deecode.walls.ui.components.*
import com.deecode.walls.ui.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onActressClick: (String) -> Unit,
    onImageClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = viewModel()
) {
    val favoriteActresses by viewModel.favoriteActresses.collectAsState()
    val favoriteImages by viewModel.favoriteImages.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CompactTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.favorites_title),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { viewModel.setSelectedTab(0) },
                    text = { Text(stringResource(R.string.tab_profiles, favoriteActresses.size)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { viewModel.setSelectedTab(1) },
                    text = { Text(stringResource(R.string.tab_images, favoriteImages.size)) }
                )
            }

            when (selectedTab) {
                0 -> {
                    if (favoriteActresses.isEmpty()) {
                        EmptyView(message = stringResource(R.string.no_favorites_profiles))
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(favoriteActresses) { favorite ->
                                ActressCard(
                                    actress = Actress(
                                        id = favorite.id,
                                        name = favorite.name,
                                        thumbnail = favorite.thumbnail,
                                        age = null,
                                        nationality = null,
                                        profession = null,
                                        source = "favorite"
                                    ),
                                    onClick = { onActressClick(favorite.id) }
                                )
                            }
                        }
                    }
                }

                1 -> {
                    if (favoriteImages.isEmpty()) {
                        EmptyView(message = stringResource(R.string.no_favorites_images))
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(favoriteImages) { favorite ->
                                val imageIndex = favoriteImages.indexOf(favorite)
                                ImageCard(
                                    imageUrl = favorite.imageUrl,
                                    onClick = { onImageClick(imageIndex) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
