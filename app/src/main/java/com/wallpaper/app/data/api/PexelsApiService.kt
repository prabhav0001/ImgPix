package com.wallpaper.app.data.api

import com.wallpaper.app.data.model.PexelsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApiService {
    
    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Header("Authorization") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): Response<PexelsResponse>
    
    @GET("v1/search")
    suspend fun searchPhotos(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): Response<PexelsResponse>
}
