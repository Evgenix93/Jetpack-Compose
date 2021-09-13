package com.project.aperture.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat

object NotificationChannels {
    const val CHANNEL_ID = "channel id"
    fun create(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, "downloads", NotificationManager.IMPORTANCE_HIGH)
                .apply { description = "download notifications"}
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }
}