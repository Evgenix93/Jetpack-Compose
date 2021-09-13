package com.project.aperture.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.aperture.data.CollectionContract

@Entity(tableName = CollectionContract.TABLE_NAME)
data class CollectionDB (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = CollectionContract.Columns.COLLECTION_ID)
    val collectionId: String,
    @ColumnInfo(name = CollectionContract.Columns.TITLE)
    val title: String,
    @ColumnInfo(name = CollectionContract.Columns.DESCRIPTION)
    val description: String?,
    @ColumnInfo(name = CollectionContract.Columns.TOTAL_PHOTOS)
    val totalPhotos: Int,
    @ColumnInfo(name = CollectionContract.Columns.COVER_PHOTO_PATH)
    val coverPhotoPath: String,
    @ColumnInfo(name = CollectionContract.Columns.AVATAR_BYTE_ARRAY)
    val avatarByteArray: ByteArray,
    @ColumnInfo(name = CollectionContract.Columns.NAME)
    val name: String,
    @ColumnInfo(name = CollectionContract.Columns.USER_NAME)
    val userName: String
        )
