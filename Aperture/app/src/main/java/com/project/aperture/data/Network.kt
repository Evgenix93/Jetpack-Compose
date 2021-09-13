package com.project.aperture.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

object Network {
    var accessToken = ""
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor {
            val request = it.request()
            val newRequest = request.newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
                 it.proceed(newRequest)
        }
        //.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

   private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.unsplash.com/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val unsplashApi: UnsplashApi
    get() = retrofit.create()
}