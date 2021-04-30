package com.skillbox.homework308

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Api {
    @GET("http://www.omdbapi.com")
   suspend fun searchMovies (
        @Query("apikey") apikey: String,
        @Query("s")query: String,
        @Query("type")type: String
    ): MoviesWrapper
}