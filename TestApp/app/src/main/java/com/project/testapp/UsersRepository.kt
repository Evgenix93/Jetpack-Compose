package com.project.testapp

import retrofit2.Call

class UsersRepository {

    fun getUsers(): Call<Result> {
        return Network.networkApi.getUsers()
    }

    fun saveUsersBD(userList: List<User>) {
        Database.instance.getUsersDao().saveUsers(userList)
    }

    fun getUsersBD(): List<User> {
        return Database.instance.getUsersDao().getUsers()
    }
}