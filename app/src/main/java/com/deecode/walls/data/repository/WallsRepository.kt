package com.deecode.walls.data.repository

import com.deecode.walls.data.local.ApiCacheDao
import com.deecode.walls.data.local.ApiCacheEntity
import com.deecode.walls.data.local.FavoriteActress
import com.deecode.walls.data.local.FavoriteDao
import com.deecode.walls.data.local.FavoriteImage
import com.deecode.walls.data.model.Actress
import com.deecode.walls.data.model.ActressDetail
import com.deecode.walls.data.model.ApiResponse
import com.deecode.walls.data.remote.ActressApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import java.lang.reflect.Type

class WallsRepository(
    private val apiService: ActressApiService,
    private val favoriteDao: FavoriteDao,
    private val apiCacheDao: ApiCacheDao
) {
    private val gson = Gson()

    private suspend fun <T> fetchWithCache(
        cacheKey: String,
        type: Type,
        networkCall: suspend () -> T
    ): Result<T> {
        try {
            val response = networkCall()
            val json = gson.toJson(response)
            apiCacheDao.insertCache(ApiCacheEntity(cacheKey, json, System.currentTimeMillis()))
            return Result.success(response)
        } catch (e: Exception) {
            val cached = apiCacheDao.getCache(cacheKey)
            if (cached != null) {
                try {
                    val data = gson.fromJson<T>(cached.data, type)
                    return Result.success(data)
                } catch (_: Exception) {
                    // Cache might be corrupted or incompatible
                }
            }
            return Result.failure(e)
        }
    }

    // API calls
    suspend fun getApiInfo(): Result<ApiResponse> {
        return try {
            val response = apiService.getApiInfo()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLatestGalleries(): Result<List<Actress>> {
        val type = object : TypeToken<List<Actress>>() {}.type
        return fetchWithCache("latest_galleries", type) {
            apiService.getLatestGalleries()
        }
    }

    suspend fun getActressesByLetter(letter: String): Result<List<Actress>> {
        val type = object : TypeToken<List<Actress>>() {}.type
        return fetchWithCache("letter_$letter", type) {
            apiService.getActressesByLetter(letter)
        }
    }

    suspend fun getActressDetail(actressId: String): Result<ActressDetail> {
        val type = object : TypeToken<ActressDetail>() {}.type
        return fetchWithCache("actress_$actressId", type) {
            apiService.getActressDetail(actressId)
        }
    }

    suspend fun getAlbumPhotos(albumUrl: String): Result<List<String>> {
        val type = object : TypeToken<List<String>>() {}.type
        return fetchWithCache("album_$albumUrl", type) {
            apiService.getAlbumPhotos(albumUrl)
        }
    }

    suspend fun searchActresses(query: String, limit: Int = 20): Result<List<Actress>> {
        // Search results are less critical to cache, but we can if we want.
        // For now, let's cache them too.
        val type = object : TypeToken<List<Actress>>() {}.type
        return fetchWithCache("search_${query}_$limit", type) {
            apiService.searchActresses(query, limit)
        }
    }

    // Favorite Actresses
    fun getAllFavoriteActresses(): Flow<List<FavoriteActress>> {
        return favoriteDao.getAllFavoriteActresses()
    }

    fun isFavoriteActress(actressId: String): Flow<Boolean> {
        return favoriteDao.isFavoriteActress(actressId)
    }

    suspend fun addFavoriteActress(actress: FavoriteActress) {
        favoriteDao.insertFavoriteActress(actress)
    }

    suspend fun removeFavoriteActress(actressId: String) {
        favoriteDao.deleteFavoriteActressById(actressId)
    }

    // Favorite Images
    fun getAllFavoriteImages(): Flow<List<FavoriteImage>> {
        return favoriteDao.getAllFavoriteImages()
    }

    suspend fun addFavoriteImage(image: FavoriteImage) {
        favoriteDao.insertFavoriteImage(image)
    }

    suspend fun removeFavoriteImage(imageUrl: String) {
        favoriteDao.deleteFavoriteImageByUrl(imageUrl)
    }

}
