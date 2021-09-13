package com.project.aperture.data

import androidx.room.*
import com.project.aperture.data.PhotoContract
import com.project.aperture.data.PhotoDB

@Dao
interface PhotoDao {

    @Query("SELECT * FROM ${PhotoContract.TABLE_NAME} WHERE ${PhotoContract.Columns.COLLECTION_ID} = :collectionId")
    suspend fun getPhotosDB(collectionId: String): List<PhotoDB>

    @Query("SELECT * FROM ${PhotoContract.TABLE_NAME} WHERE ${PhotoContract.Columns.PHOTO_ID} = :id")
    suspend fun getPhotoDB(id: String): PhotoDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePhotosDB(photos: List<PhotoDB>)

    @Query("DELETE FROM ${PhotoContract.TABLE_NAME} WHERE ${PhotoContract.Columns.COLLECTION_ID} = :collectionId")
    suspend fun deletePhotosDB(collectionId: String)

}