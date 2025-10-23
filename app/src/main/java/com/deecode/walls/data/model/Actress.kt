package com.deecode.walls.data.model

import com.google.gson.annotations.SerializedName

data class Actress(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("thumbnail")
    val thumbnail: String?,

    @SerializedName("age")
    val age: Int?,

    @SerializedName("nationality")
    val nationality: String?,

    @SerializedName("profession")
    val profession: String?,

    @SerializedName("source")
    val source: String
)

data class ActressDetail(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("images")
    val images: List<String>,

    @SerializedName("albums")
    val albums: List<Album>,

    @SerializedName("age")
    val age: Int?,

    @SerializedName("birth_date")
    val birthDate: String?,

    @SerializedName("nationality")
    val nationality: String?,

    @SerializedName("profession")
    val profession: String?,

    @SerializedName("height")
    val height: String?,

    @SerializedName("bio")
    val bio: String?,

    @SerializedName("known_for")
    val knownFor: List<String>,

    @SerializedName("social_media")
    val socialMedia: Map<String, String>?,

    @SerializedName("source")
    val source: String,

    @SerializedName("last_updated")
    val lastUpdated: String?
)

data class Album(
    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("thumbnail")
    val thumbnail: String?
)

data class ApiResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("version")
    val version: String?,

    @SerializedName("status")
    val status: String?
)
