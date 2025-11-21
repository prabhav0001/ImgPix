package com.deecode.walls.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GalleryDao {
    @Query("SELECT * FROM galleries ORDER BY cachedAt DESC")
    fun getAllGalleries(): Flow<List<GalleryEntity>>

    @Query("SELECT * FROM galleries ORDER BY cachedAt DESC")
    suspend fun getGalleriesSnapshot(): List<GalleryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGalleries(galleries: List<GalleryEntity>)

    @Query("DELETE FROM galleries")
    suspend fun clearGalleries()
}
