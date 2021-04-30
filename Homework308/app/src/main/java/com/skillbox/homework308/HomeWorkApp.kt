package com.skillbox.homework308

import android.app.Application

class HomeWorkApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Database.create(this)
    }
}