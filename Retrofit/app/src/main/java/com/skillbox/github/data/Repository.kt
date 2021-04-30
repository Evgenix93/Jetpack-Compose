package com.skillbox.github.data

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass (generateAdapter = true)
@Parcelize
data class Repository (
    val id:Int,
    val name:String,
    val owner: Owner
): Parcelable
