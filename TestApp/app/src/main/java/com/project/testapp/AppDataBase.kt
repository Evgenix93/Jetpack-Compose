package com.project.testapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = AppDataBase.VERSION)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getUsersDao(): UsersDao

    companion object {
        const val NAME = "app_data_base"
         const val VERSION = 1
    }
}