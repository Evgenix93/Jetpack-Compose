package com.project.testapp

import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Network {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://reqres.in")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val networkApi: NetworkApi = retrofit.create()
}