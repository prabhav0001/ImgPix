package com.deecode.walls.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_actresses")
data class FavoriteActress(
    @PrimaryKey
    val id: String,
    val name: String,
    val thumbnail: String?,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorite_images")
data class FavoriteImage(
    @PrimaryKey
    val imageUrl: String,
    val actressId: String,
    val actressName: String,
    val addedAt: Long = System.currentTimeMillis()
)
