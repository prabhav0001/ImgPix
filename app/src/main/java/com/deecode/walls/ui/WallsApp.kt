package com.deecode.walls.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deecode.walls.R
import com.deecode.walls.navigation.Screen
import com.deecode.walls.navigation.WallsNavGraph
import com.deecode.walls.ui.components.NoInternetView
import com.deecode.walls.util.NetworkConnectivityObserver

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val labelResId: Int
)

@Composable
fun WallsApp(
    onThemeToggle: () -> Unit
) {
    val context = LocalContext.current
    val networkObserver = remember { NetworkConnectivityObserver(context) }
    val isConnected by networkObserver.observe().collectAsState(initial = networkObserver.isConnected())

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, Icons.Default.Home, R.string.nav_home),
        BottomNavItem(Screen.Browse.route, Icons.AutoMirrored.Filled.List, R.string.nav_browse),
        BottomNavItem(Screen.Search.route, Icons.Default.Search, R.string.nav_search),
        BottomNavItem(Screen.Favorites.route, Icons.Default.Favorite, R.string.nav_favorites)
    )

    // Show bottom bar only on main screens
    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    if (!isConnected) {
        // Show no internet screen
        NoInternetView(
            onRetry = {
                // Just checking connectivity, user needs to enable internet manually
            }
        )
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                AnimatedVisibility(
                    visible = showBottomBar,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            val label = stringResource(item.labelResId)
                            NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = label) },
                                label = { Text(label) },
                                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                WallsNavGraph(
                    navController = navController,
                    onThemeToggle = onThemeToggle
                )
            }
        }
    }
}
