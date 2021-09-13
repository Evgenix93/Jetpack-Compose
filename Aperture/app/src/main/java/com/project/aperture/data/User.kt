package com.project.aperture.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class User(
    @Json(name = "username")
    val userName: String,
    val name: String,
    @Json(name = "profile_image")
    val avatar: Map<String, String>,
    val bio: String?
)
