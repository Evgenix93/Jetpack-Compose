package com.skillbox.github.data

import retrofit2.Call
import retrofit2.http.*

interface GithubApi {
    @GET("/user")
   fun currentUser (): Call<User>

    @GET("/repositories")
    fun getRepositoryList ():Call<List<Repository>>

    @PUT("/user/starred/{owner}/{repo}")
    fun putStar (
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<Unit>

    @DELETE("/user/starred/{owner}/{repo}")
    fun removeStar(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<Unit>

    @GET ("/user/starred/{owner}/{repo}")
    fun checkStar(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<Int>
}