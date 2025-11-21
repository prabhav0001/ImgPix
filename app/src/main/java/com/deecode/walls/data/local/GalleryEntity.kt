package com.deecode.walls.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "galleries")
data class GalleryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val thumbnail: String?,
    val cachedAt: Long = System.currentTimeMillis()
)
