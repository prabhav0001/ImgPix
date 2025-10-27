package com.deecode.walls.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deecode.walls.ui.common.UiState
import com.deecode.walls.ui.components.*
import com.deecode.walls.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onActressClick: (String) -> Unit,
    onThemeToggle: () -> Unit,
    onAboutClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.latestGalleries.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadLatestGalleries()
    }

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success || uiState is UiState.Error) {
            isRefreshing = false
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CompactTopAppBar(
                title = {
                    Text(
                        "Walls",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    )
                },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = Icons.Default.Brightness6,
                            contentDescription = "Toggle theme"
                        )
                    }
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("About") },
                                onClick = {
                                    showMenu = false
                                    onAboutClick()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "About"
                                    )
                                }
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.loadLatestGalleries()
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is UiState.Loading -> {
                    if (!isRefreshing) {
                        LoadingView()
                    }
                }

                is UiState.Success -> {
                    val actresses = state.data

                    if (actresses.isEmpty()) {
                        EmptyView(
                            message = "No galleries available"
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(
                                start = 12.dp,
                                end = 12.dp,
                                top = 12.dp,
                                bottom = 12.dp
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
                        message = state.message
                    )
                }

                else -> {}
            }
        }
    }
}
