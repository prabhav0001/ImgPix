package com.deecode.walls.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deecode.walls.ui.common.UiState
import com.deecode.walls.ui.components.*
import com.deecode.walls.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onActressClick: (String) -> Unit,
    onThemeToggle: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.latestGalleries.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLatestGalleries()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Walls") },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = Icons.Default.Brightness6,
                            contentDescription = "Toggle theme"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is UiState.Loading -> {
                LoadingView(modifier = Modifier.padding(paddingValues))
            }

            is UiState.Success -> {
                val actresses = state.data

                if (actresses.isEmpty()) {
                    EmptyView(
                        message = "No galleries available",
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
                        items(actresses) { actress ->
                            ActressCard(
                                actress = actress,
                                onClick = { onActressClick(actress.id) }
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

            else -> {
                // Idle state
            }
        }
    }
}
