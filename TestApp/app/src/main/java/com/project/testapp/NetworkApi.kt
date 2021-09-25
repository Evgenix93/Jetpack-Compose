package com.project.testapp

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface NetworkApi {

    @GET("/api/users")
    fun getUsers(): Call<Result>

}