package com.project.aperture.data

import android.app.Application
import android.util.Log
import com.project.aperture.data.Database
import com.project.aperture.data.NotificationChannels

class ApertureApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Database.init(this)
        NotificationChannels.create(this)
    }
}