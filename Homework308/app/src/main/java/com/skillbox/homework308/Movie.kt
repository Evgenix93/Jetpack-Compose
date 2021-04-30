package com.skillbox.homework308

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
@Entity(tableName = MovieContract.TABLE_NAME)
data class Movie  (
        @Json(name = "imdbID")
        @PrimaryKey
        @ColumnInfo(name = MovieContract.Columns.ID)
        val id: String,
        @Json(name = "Title")
        @ColumnInfo(name = MovieContract.Columns.TITLE)
        val title: String,
        @Json(name = "Type")
        @ColumnInfo(name = MovieContract.Columns.TYPE)
        val type: String,
        @Json(name = "Year")
        @ColumnInfo(name = MovieContract.Columns.YEAR)
        val year: String,
        @Json(name = "Poster")
        @ColumnInfo(name = MovieContract.Columns.POSTER)
        val poster: String
)
