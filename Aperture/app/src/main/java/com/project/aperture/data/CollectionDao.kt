package com.project.aperture.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CollectionDao {

    @Query("SELECT * FROM ${CollectionContract.TABLE_NAME}")
    suspend fun getCollectionsDB(): List<CollectionDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCollectionsDB(collectionsDB: List<CollectionDB>)

    @Query("SELECT * FROM ${CollectionContract.TABLE_NAME} WHERE ${CollectionContract.Columns.COLLECTION_ID} = :collectionId")
    suspend fun getCollectionDB(collectionId: String): CollectionDB

    @Query("DELETE FROM ${CollectionContract.TABLE_NAME}")
    suspend fun deleteCollectionsDB()
}