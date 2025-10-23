package com.deecode.walls.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.deecode.walls.ui.screens.*
import java.net.URLDecoder

@Composable
fun WallsNavGraph(
    navController: NavHostController,
    onThemeToggle: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            HomeScreen(
                onActressClick = { actressId ->
                    navController.navigate(Screen.ActressDetail.createRoute(actressId))
                },
                onThemeToggle = onThemeToggle
            )
        }

        composable(
            route = Screen.Browse.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            BrowseScreen(
                onActressClick = { actressId ->
                    navController.navigate(Screen.ActressDetail.createRoute(actressId))
                }
            )
        }

        composable(
            route = Screen.Search.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            SearchScreen(
                onActressClick = { actressId ->
                    navController.navigate(Screen.ActressDetail.createRoute(actressId))
                }
            )
        }

        composable(
            route = Screen.Favorites.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            FavoritesScreen(
                onActressClick = { actressId ->
                    navController.navigate(Screen.ActressDetail.createRoute(actressId))
                },
                onImageClick = { imageUrl, actressName ->
                    navController.navigate(Screen.ImageViewer.createRoute(imageUrl, actressName))
                }
            )
        }

        composable(
            route = Screen.ActressDetail.route,
            arguments = listOf(
                navArgument("actressId") { type = NavType.StringType }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { backStackEntry ->
            val actressId = backStackEntry.arguments?.getString("actressId") ?: return@composable
            ActressDetailScreen(
                actressId = actressId,
                onBackClick = { navController.popBackStack() },
                onImageClick = { imageUrl, actressName ->
                    navController.navigate(Screen.ImageViewer.createRoute(imageUrl, actressName))
                }
            )
        }

        composable(
            route = Screen.AlbumDetail.route,
            arguments = listOf(
                navArgument("albumUrl") { type = NavType.StringType },
                navArgument("albumName") { type = NavType.StringType }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { backStackEntry ->
            val albumUrl = backStackEntry.arguments?.getString("albumUrl")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: return@composable
            val albumName = backStackEntry.arguments?.getString("albumName")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: return@composable

            AlbumDetailScreen(
                albumUrl = albumUrl,
                albumName = albumName,
                onBackClick = { navController.popBackStack() },
                onImageClick = { imageUrl ->
                    navController.navigate(Screen.ImageViewer.createRoute(imageUrl, albumName))
                }
            )
        }

        composable(
            route = Screen.ImageViewer.route,
            arguments = listOf(
                navArgument("imageUrl") { type = NavType.StringType },
                navArgument("actressName") { type = NavType.StringType }
            ),
            enterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) { backStackEntry ->
            val imageUrl = backStackEntry.arguments?.getString("imageUrl")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: return@composable
            val actressName = backStackEntry.arguments?.getString("actressName")?.let {
                URLDecoder.decode(it, "UTF-8")
            }?.takeIf { it.isNotBlank() }

            ImageViewerScreen(
                imageUrl = imageUrl,
                actressName = actressName,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
