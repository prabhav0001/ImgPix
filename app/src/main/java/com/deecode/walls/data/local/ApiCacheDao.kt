package com.deecode.walls.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ApiCacheDao {
    @Query("SELECT * FROM api_cache WHERE `key` = :key")
    suspend fun getCache(key: String): ApiCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(cache: ApiCacheEntity)

    @Query("DELETE FROM api_cache WHERE timestamp < :threshold")
    suspend fun clearOldCache(threshold: Long)

    @Query("DELETE FROM api_cache")
    suspend fun clearAllCache()
}
