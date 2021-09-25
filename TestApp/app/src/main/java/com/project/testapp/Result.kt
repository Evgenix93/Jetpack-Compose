package com.project.testapp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    val page: Int,
    @Json(name = "per_page")
    val perPage: Int,
    val total: Int,
    @Json(name = "total_pages")
    val totalPages: Int,
    val data: List<User>
)
