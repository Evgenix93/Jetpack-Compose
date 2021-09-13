package com.project.aperture.data

import android.content.Context
import androidx.work.*
import com.project.aperture.utils.DownloadWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class PhotosRepository(private val context: Context) {
    private val photoDao = Database.instance.photoDao()
    private val collectionDao = Database.instance.collectionDao()

    suspend fun getPhotos(page: Int): List<Photo> {
        return try {
            Network.unsplashApi.getPhotos("/photos", page)
        } catch (e: IOException) {
            emptyList()
        }
    }

    suspend fun getPhotosDB(collectionId: String): List<PhotoDB> {
        return photoDao.getPhotosDB(collectionId)
    }

    suspend fun savePhotosDB(photos: List<Photo>, collectionId: String) {
        val fileName = if (collectionId.isNotEmpty())
        "Collection${collectionId}Photo"
        else
          "Photo"
        try {
            val photosDB = photos.mapIndexed { index, photo ->
                val photoFile = File(context.filesDir, "$fileName$index.jpg").apply {
                    outputStream().use { outputStream ->
                        Network.unsplashApi.downloadPhoto(photo.urls["raw"]!!)
                            .byteStream()
                            .use { inputStream ->
                                inputStream.copyTo(outputStream)
                            }
                    }
                }
                val avatarByteArray =
                    Network.unsplashApi.downloadPhoto(photo.user.avatar["small"]!!).bytes()
                val likedByUser = if (photo.likedByUser)
                    1
                else
                    0
                val id = if (collectionId.isNotEmpty())
                    "Collection${collectionId}Photo$index"
                else
                    "Photo$index"
                PhotoDB(
                    id,
                    photo.id,
                    collectionId,
                    photo.likes,
                    likedByUser,
                    photo.user.name,
                    photo.user.userName,
                    photoFile.path,
                    avatarByteArray
                )
            }
            photoDao.savePhotosDB(photosDB)
        }catch (error: Throwable){
            photoDao.deletePhotosDB(collectionId)
        }
    }

    suspend fun getPhoto(id: String?): Photo? {
        if (id == null)
            return null
        return try {
            Network.unsplashApi.getPhoto("/photos/$id")
        } catch (error: IOException) {
            null
        }
    }


    fun launchWorkManager(id: String, name: String, url: String) {
        val inputData = workDataOf(
            DownloadWorker.ID_KEY to id,
            DownloadWorker.NAME_KEY to name,
            DownloadWorker.URL_KEY to url
        )
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            DownloadWorker.DOWNLOAD_WORK_ID,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    suspend fun likePhoto(id: String, likedByUser: Boolean): Photo? {
        return try {
            if (likedByUser)
                Network.unsplashApi.unlikePhoto("/photos/$id/like").photo
            else
                Network.unsplashApi.likePhoto("/photos/$id/like").photo
        } catch (e: IOException) {
            null
        }
    }

    suspend fun searchPhotos(query: String): List<Photo> {
        return Network.unsplashApi.searchPhotos("/search/photos", query).results
    }

    suspend fun getCollections(page: Int): List<Collection> {
        return try {
            Network.unsplashApi.getCollections("/collections", page)
        } catch (e: IOException) {
            emptyList()
        }
    }

    suspend fun getCollectionsDB(): List<CollectionDB> {
        return collectionDao.getCollectionsDB()
    }

    suspend fun saveCollectionsDB(collections: List<Collection>) {
        try {
            val collectionsDB = collections.mapIndexed { index, collection ->
                val coverPhotoFile = File(context.filesDir, "CoverPhoto$index.jpg").apply {
                    if (collection.coverPhoto != null)
                        outputStream().use { outputStream ->
                            Network.unsplashApi.downloadPhoto(collection.coverPhoto.urls["raw"]!!)
                                .byteStream()
                                .use { inputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                        }
                }
                val avatarByteArray =
                    Network.unsplashApi.downloadPhoto(collection.user.avatar["small"]!!).bytes()
                CollectionDB(
                    index,
                    collection.id,
                    collection.title,
                    collection.description,
                    collection.totalPhotos,
                    coverPhotoFile.path,
                    avatarByteArray,
                    collection.user.name,
                    collection.user.userName
                )
            }
            collectionDao.saveCollectionsDB(collectionsDB)
        }catch (error: Throwable){
            collectionDao.deleteCollectionsDB()
        }
    }

    suspend fun getCollection(id: String): Collection? {
        return try {
            Network.unsplashApi.getCollection("/collections/$id")
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getCollectionDB(id: String): CollectionDB {
        return collectionDao.getCollectionDB(id)
    }

    suspend fun getCollectionPhotos(id: String, page: Int): List<Photo> {
        return try {
            Network.unsplashApi.getPhotos("/collections/$id/photos", page)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getProfile(): Profile? {
        return try {
            Network.unsplashApi.getProfile()
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getProfilePhotos(userName: String?, page: Int): List<Photo> {
        return if (userName == null)
            emptyList()
        else
            try {
                Network.unsplashApi.getPhotos("/users/$userName/photos", page)
            } catch (e: IOException) {
                emptyList()
            }
    }

    suspend fun getUser(userName: String?): User? {
       return if (userName == null)
             null
        else try {
            Network.unsplashApi.getUser("/users/$userName")
        } catch (e: IOException) {
            null
        }
    }

    suspend fun getLikedPhotos(userName: String?, page: Int): List<Photo> {
       return if (userName == null)
             emptyList()
        else try {
            Network.unsplashApi.getPhotos("/users/$userName/likes", page)
        } catch (e: IOException) {
            emptyList()
        }
    }

    suspend fun getProfileCollections(userName: String?, page: Int): List<Collection> {
        return if (userName == null)
             emptyList()
        else try {
            Network.unsplashApi.getCollections("/users/$userName/collections", page)
        } catch (e: IOException) {
            emptyList()
        }
    }

    suspend fun trackDownloads(id: String){
     Network.unsplashApi.trackDownloads("/photos/$id/download")
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            context.getSharedPreferences("shared prefs", Context.MODE_PRIVATE).edit().clear()
                .apply()
            context.filesDir.listFiles().orEmpty().forEach {
                it.delete()
            }
            Database.instance.clearAllTables()
        }
    }
}