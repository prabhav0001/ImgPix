package com.wallpaper.app.data.repository

import com.wallpaper.app.BuildConfig
import com.wallpaper.app.data.api.PexelsApiService
import com.wallpaper.app.data.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WallpaperRepository(private val apiService: PexelsApiService) {

    private val apiKey = BuildConfig.PEXELS_API_KEY

    suspend fun getCuratedWallpapers(page: Int = 1, perPage: Int = 30): Result<List<Photo>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCuratedPhotos(apiKey, page, perPage)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.photos)
                } else {
                    Result.failure(Exception("Failed to load wallpapers: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun searchWallpapers(
        query: String,
        page: Int = 1,
        perPage: Int = 30
    ): Result<List<Photo>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchPhotos(apiKey, query, page, perPage)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.photos)
                } else {
                    Result.failure(Exception("Failed to search wallpapers: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
