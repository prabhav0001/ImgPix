package com.deecode.walls.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    // Favorite Actresses
    @Query("SELECT * FROM favorite_actresses ORDER BY addedAt DESC")
    fun getAllFavoriteActresses(): Flow<List<FavoriteActress>>

    @Query("SELECT * FROM favorite_actresses WHERE id = :actressId")
    fun getFavoriteActress(actressId: String): Flow<FavoriteActress?>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_actresses WHERE id = :actressId)")
    fun isFavoriteActress(actressId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteActress(actress: FavoriteActress)

    @Delete
    suspend fun deleteFavoriteActress(actress: FavoriteActress)

    @Query("DELETE FROM favorite_actresses WHERE id = :actressId")
    suspend fun deleteFavoriteActressById(actressId: String)

    // Favorite Images
    @Query("SELECT * FROM favorite_images ORDER BY addedAt DESC")
    fun getAllFavoriteImages(): Flow<List<FavoriteImage>>

    @Query("SELECT * FROM favorite_images WHERE actressId = :actressId ORDER BY addedAt DESC")
    fun getFavoriteImagesByActress(actressId: String): Flow<List<FavoriteImage>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_images WHERE imageUrl = :imageUrl)")
    fun isFavoriteImage(imageUrl: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteImage(image: FavoriteImage)

    @Delete
    suspend fun deleteFavoriteImage(image: FavoriteImage)

    @Query("DELETE FROM favorite_images WHERE imageUrl = :imageUrl")
    suspend fun deleteFavoriteImageByUrl(imageUrl: String)

    @Query("SELECT COUNT(*) FROM favorite_actresses")
    fun getFavoriteActressCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM favorite_images")
    fun getFavoriteImageCount(): Flow<Int>
}
