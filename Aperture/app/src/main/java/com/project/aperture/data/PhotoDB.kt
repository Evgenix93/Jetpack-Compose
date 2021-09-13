package com.project.aperture.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.aperture.data.PhotoContract

@Entity(tableName = PhotoContract.TABLE_NAME)
data class PhotoDB (
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = PhotoContract.Columns.PHOTO_ID)
    val photoId: String,
    @ColumnInfo(name = PhotoContract.Columns.COLLECTION_ID)
    val collectionId: String,
    @ColumnInfo(name = PhotoContract.Columns.LIKES)
    val likes: Int,
    @ColumnInfo(name = PhotoContract.Columns.LIKED_BY_USER)
    val likedByUser: Int,
    @ColumnInfo(name = PhotoContract.Columns.NAME)
    val name: String,
    @ColumnInfo(name = PhotoContract.Columns.USER_NAME)
    val userName: String,
    @ColumnInfo(name = PhotoContract.Columns.PHOTO_PATH)
    val photoPath: String,
    @ColumnInfo(name = PhotoContract.Columns.AVATAR)
    val avatar: ByteArray
        )
