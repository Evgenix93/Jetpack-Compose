package com.project.aperture.data

import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class Location(
    val city: String?,
    val country: String?,
    val position: Map<String, Float>
)
