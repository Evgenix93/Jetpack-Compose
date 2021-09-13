package com.project.aperture.data

import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class Tag(
    val title: String
)
