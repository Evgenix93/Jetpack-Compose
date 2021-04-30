package com.skillbox.github.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val login: String,
    @Json(name = "avatar_url")
val avatar: String
)
