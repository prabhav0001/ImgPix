package com.deecode.walls.data.repository

import com.deecode.walls.data.local.FavoriteActress
import com.deecode.walls.data.local.FavoriteDao
import com.deecode.walls.data.local.FavoriteImage
import com.deecode.walls.data.model.Actress
import com.deecode.walls.data.model.ActressDetail
import com.deecode.walls.data.remote.ActressApiService
import kotlinx.coroutines.flow.Flow

class WallsRepository(
    private val apiService: ActressApiService,
    private val favoriteDao: FavoriteDao
) {

    // API calls
    suspend fun getLatestGalleries(): Result<List<Actress>> {
        return try {
            val response = apiService.getLatestGalleries()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getActressesByLetter(letter: String): Result<List<Actress>> {
        return try {
            val response = apiService.getActressesByLetter(letter)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getActressDetail(actressId: String): Result<ActressDetail> {
        return try {
            val response = apiService.getActressDetail(actressId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumPhotos(albumUrl: String): Result<List<String>> {
        return try {
            val response = apiService.getAlbumPhotos(albumUrl)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchActresses(query: String, limit: Int = 20): Result<List<Actress>> {
        return try {
            val response = apiService.searchActresses(query, limit)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
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

    fun isFavoriteImage(imageUrl: String): Flow<Boolean> {
        return favoriteDao.isFavoriteImage(imageUrl)
    }

    suspend fun addFavoriteImage(image: FavoriteImage) {
        favoriteDao.insertFavoriteImage(image)
    }

    suspend fun removeFavoriteImage(imageUrl: String) {
        favoriteDao.deleteFavoriteImageByUrl(imageUrl)
    }

}
