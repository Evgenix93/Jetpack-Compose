package com.project.testapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = UserContract.TABLE_NAME)
@JsonClass(generateAdapter = true)
data class User(
    @PrimaryKey
    @ColumnInfo(name = UserContract.Columns.ID)
    val id: Int,
    @ColumnInfo(name = UserContract.Columns.EMAIL)
    val email: String,
    @ColumnInfo(name = UserContract.Columns.FIRST_NAME)
    @Json(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = UserContract.Columns.LAST_NAME)
    @Json(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = UserContract.Columns.AVATAR)
    val avatar: String
)


