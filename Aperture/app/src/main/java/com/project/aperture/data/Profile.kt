package com.project.aperture.data

import com.project.aperture.data.Location
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Profile(
    @Json(name = "username")
    val userName: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "last_name")
    val lastName: String,
    val bio: String?,
    val location: Location?,
    @Json(name = "total_likes")
    val totalLikes: String,
    @Json(name = "total_photos")
    val totalPhotos: String,
    @Json(name = "total_collections")
    val totalCollections: String,
    val downloads: Int,
    val email: String,
)
