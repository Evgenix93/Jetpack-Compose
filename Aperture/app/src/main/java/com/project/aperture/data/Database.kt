package com.project.aperture.data

import android.content.Context
import androidx.room.Room

object Database {
    lateinit var instance: AppDataBase

    fun init(context: Context) {
        instance = Room.databaseBuilder(context, AppDataBase::class.java, AppDataBase.NAME).build()
    }
}