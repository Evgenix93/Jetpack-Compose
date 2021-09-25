package com.project.testapp

import android.app.Application


class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Database.createInstance(this)
    }
}