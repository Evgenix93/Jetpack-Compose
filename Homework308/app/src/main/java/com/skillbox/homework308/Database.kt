package com.skillbox.homework308

import android.content.Context
import androidx.room.Room

object Database {
    lateinit var instance: MovieDatabase

    fun create (context: Context){
        instance = Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            MovieDatabase.NAME
        ).build()
    }
}