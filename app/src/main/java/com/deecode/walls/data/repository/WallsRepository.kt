package com.deecode.walls.data.repository

import com.deecode.walls.data.local.FavoriteActress
import com.deecode.walls.data.local.FavoriteDao
import com.deecode.walls.data.local.FavoriteImage
import com.deecode.walls.data.local.GalleryDao
import com.deecode.walls.data.local.GalleryEntity
import com.deecode.walls.data.model.Actress
import com.deecode.walls.data.model.ActressDetail
import com.deecode.walls.data.model.ApiResponse
import com.deecode.walls.data.remote.ActressApiService
import kotlinx.coroutines.flow.Flow

class WallsRepository(
    private val apiService: ActressApiService,
    private val favoriteDao: FavoriteDao,
    private val galleryDao: GalleryDao
) {

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
        return try {
            val response = apiService.getLatestGalleries()
            // Cache the result
            val galleryEntities = response.map { actress ->
                GalleryEntity(
                    id = actress.id,
                    name = actress.name,
                    thumbnail = actress.thumbnail
                )
            }
            galleryDao.clearGalleries()
            galleryDao.insertGalleries(galleryEntities)
            Result.success(response)
        } catch (e: Exception) {
            // Try to get from cache
            try {
                val cachedGalleries = galleryDao.getGalleriesSnapshot()
                if (cachedGalleries.isNotEmpty()) {
                    val actresses = cachedGalleries.map { entity ->
                        Actress(
                            id = entity.id,
                            name = entity.name,
                            thumbnail = entity.thumbnail,
                            age = null,
                            nationality = null,
                            profession = null,
                            source = "local_cache"
                        )
                    }
                    Result.success(actresses)
                } else {
                    Result.failure(e)
                }
            } catch (cacheError: Exception) {
                Result.failure(e)
            }
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

    suspend fun addFavoriteImage(image: FavoriteImage) {
        favoriteDao.insertFavoriteImage(image)
    }

    suspend fun removeFavoriteImage(imageUrl: String) {
        favoriteDao.deleteFavoriteImageByUrl(imageUrl)
    }

}
