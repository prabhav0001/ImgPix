package com.deecode.walls.data.remote

import com.deecode.walls.data.model.Actress
import com.deecode.walls.data.model.ActressDetail
import com.deecode.walls.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActressApiService {

    @GET("/")
    suspend fun getApiInfo(): ApiResponse

    @GET("/api/ragalahari/latest")
    suspend fun getLatestGalleries(): List<Actress>

    @GET("/api/ragalahari/letter/{letter}")
    suspend fun getActressesByLetter(@Path("letter") letter: String): List<Actress>

    @GET("/api/actress/{actress_id}")
    suspend fun getActressDetail(@Path("actress_id") actressId: String): ActressDetail

    @GET("/api/album/photos")
    suspend fun getAlbumPhotos(@Query("album_url") albumUrl: String): List<String>

    @GET("/api/search")
    suspend fun searchActresses(
        @Query("query") query: String,
        @Query("limit") limit: Int = 20
    ): List<Actress>
}
