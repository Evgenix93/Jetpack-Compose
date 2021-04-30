package com.skillbox.github.data

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass (generateAdapter = true)
@Parcelize
data class Owner (
    val login: String
): Parcelable
