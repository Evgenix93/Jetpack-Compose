package com.project.aperture.data

import com.project.aperture.data.Photo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoWrapper(
    val photo: Photo
)
