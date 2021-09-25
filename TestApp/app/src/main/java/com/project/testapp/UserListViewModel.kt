package com.project.testapp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bumptech.glide.util.Executors
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException

class UserListViewModel : ViewModel() {
    private val usersRepository = UsersRepository()

    fun getUsers(): Single<List<User>> {
        return Single.create<List<User>> {
            try {
                val userList =
                    usersRepository.getUsers().execute().body()?.data ?: error("download error")
                it.onSuccess(userList)
            } catch (e: Throwable) {
                it.onError(e)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.from(Executors.mainThreadExecutor()))
    }

    fun getUsersBD(): Single<List<User>> {
        return Single.create<List<User>> {
            try {
                it.onSuccess(usersRepository.getUsersBD())
            } catch (e: Throwable) {
                it.onError(e)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.from(Executors.mainThreadExecutor()))
    }

    fun saveUsersBD(userList: List<User>): Single<Unit> {
        return Single.create<Unit> {
            try {
                usersRepository.saveUsersBD(userList)
                it.onSuccess(Unit)
            } catch (e: Throwable) {
                it.onError(e)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.from(Executors.mainThreadExecutor()))
    }
}