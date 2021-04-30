package com.skillbox.homework32_5

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
        val name: String,
        val price: Int,
        val image: String
): Parcelable
