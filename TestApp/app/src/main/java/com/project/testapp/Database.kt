package com.project.testapp

import android.content.Context
import androidx.room.Room

object Database {
    lateinit var instance: AppDataBase

    fun createInstance(context: Context) {
        instance = Room.databaseBuilder(context, AppDataBase::class.java, AppDataBase.NAME).build()
    }
}