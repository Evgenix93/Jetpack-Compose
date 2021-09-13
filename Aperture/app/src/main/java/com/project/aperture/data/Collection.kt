package com.project.aperture.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Collection(
    val id: String,
    val title: String,
    val description: String?,
    @Json(name = "total_photos")
    val totalPhotos: Int,
    @Json(name = "cover_photo")
    val coverPhoto: Photo?,
    val user: User
)
