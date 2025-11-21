package com.deecode.walls.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.deecode.walls.data.local.WallsDatabase
import com.deecode.walls.data.remote.RetrofitInstance
import com.deecode.walls.data.repository.WallsRepository

/**
 * Base ViewModel that provides repository initialization for all ViewModels.
 * Eliminates duplicate initialization code across ViewModels.
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val repository: WallsRepository

    init {
        val database = WallsDatabase.getDatabase(application)
        repository = WallsRepository(RetrofitInstance.api, database.favoriteDao(), database.galleryDao())
    }
}
