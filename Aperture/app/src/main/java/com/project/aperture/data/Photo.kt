package com.project.aperture.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Photo(
    val id: String,
    val likes: Int,
    @Json(name = "liked_by_user")
    val likedByUser: Boolean,
    val downloads: Int?,
    val exif: Exif?,
    val location: Location?,
    val tags: List<Tag>?,
    val user: User,
    val urls: Map<String, String>
)
