package com.deecode.walls.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Browse : Screen("browse")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object ActressDetail : Screen("actress_detail/{actressId}") {
        fun createRoute(actressId: String) = "actress_detail/$actressId"
    }
    object AlbumDetail : Screen("album_detail/{albumUrl}/{albumName}") {
        fun createRoute(albumUrl: String, albumName: String): String {
            val encodedUrl = java.net.URLEncoder.encode(albumUrl, "UTF-8")
            val encodedName = java.net.URLEncoder.encode(albumName, "UTF-8")
            return "album_detail/$encodedUrl/$encodedName"
        }
    }
    object ImageViewer : Screen("image_viewer/{imageUrl}/{actressName}") {
        fun createRoute(imageUrl: String, actressName: String?): String {
            val encodedUrl = java.net.URLEncoder.encode(imageUrl, "UTF-8")
            val encodedName = java.net.URLEncoder.encode(actressName ?: "", "UTF-8")
            return "image_viewer/$encodedUrl/$encodedName"
        }
    }
}
