package com.skillbox.github.data

import okhttp3.OkHttp
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
                    .addHeader("Authorization", "token $accessToken")
                    .build()
                 it.proceed(newRequest)
        }
        .build()

   private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val githubApi: GithubApi
    get() = retrofit.create()
}