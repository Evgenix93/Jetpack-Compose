package com.skillbox.homework308

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesWrapper (
    @Json(name = "Search") val movies: List<Movie>?
    )