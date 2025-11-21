package com.deecode.walls.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_cache")
data class ApiCacheEntity(
    @PrimaryKey val key: String,
    val data: String, // JSON content
    val timestamp: Long
)
