package com.project.aperture.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PhotoDB::class, CollectionDB::class], version = AppDataBase.VERSION)
abstract class AppDataBase: RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun collectionDao(): CollectionDao

    companion object {
        const val VERSION = 1
        const val NAME = "app database"
    }
}