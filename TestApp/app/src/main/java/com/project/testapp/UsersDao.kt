package com.project.testapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsersDao {
    @Query("SELECT * FROM ${UserContract.TABLE_NAME}")
    fun getUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUsers(userList: List<User>)
}