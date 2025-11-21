package com.deecode.walls.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteActress::class, FavoriteImage::class, ApiCacheEntity::class],
    version = 2,
    exportSchema = false
)
abstract class WallsDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao
    abstract fun apiCacheDao(): ApiCacheDao

    companion object {
        @Volatile
        private var INSTANCE: WallsDatabase? = null

        fun getDatabase(context: Context): WallsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WallsDatabase::class.java,
                    "walls_database"
                )
                .fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
