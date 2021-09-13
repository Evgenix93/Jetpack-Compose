package com.project.aperture.data

import com.project.aperture.data.*
import com.project.aperture.data.Collection
import okhttp3.ResponseBody
import retrofit2.http.*

interface UnsplashApi {

    @GET
   suspend fun getPhotos (@Url url: String, @Query(value = "page") page: Int) : List<Photo>

   @GET
   suspend fun getPhoto(@Url url: String): Photo

   @POST
   suspend fun likePhoto(@Url url: String): PhotoWrapper

   @DELETE
   suspend fun unlikePhoto(@Url url: String): PhotoWrapper

   @GET
   suspend fun downloadPhoto(@Url url: String): ResponseBody

   @GET
   suspend fun trackDownloads(@Url url: String)

   @GET
   suspend fun searchPhotos(@Url url: String, @Query(value = "query") query: String): SearchResult

   @GET
   suspend fun getCollections(@Url url: String, @Query(value = "page") page: Int): List<Collection>

   @GET
   suspend fun getCollection(@Url url: String): Collection


   @GET("/me")
   suspend fun getProfile(): Profile

   @GET
   suspend fun getUser(@Url url: String): User

}