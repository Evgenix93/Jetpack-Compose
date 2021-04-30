package com.skillbox.homework308

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = [Movie::class], version = MovieDatabase.VERSION, exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object{
        const val VERSION = 1
        const val NAME = "movie database"
    }
}