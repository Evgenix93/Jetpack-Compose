package com.skillbox.homework308

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Network {
    const val API_KEY = "4b3612a4"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://www.omdbapi.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val api: Api
    get() = retrofit.create()
}