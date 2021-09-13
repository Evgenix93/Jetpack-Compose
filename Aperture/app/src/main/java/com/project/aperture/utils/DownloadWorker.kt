package com.project.aperture.utils

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.project.aperture.R
import com.project.aperture.data.Network
import com.project.aperture.data.NotificationChannels


class DownloadWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val name = inputData.getString(NAME_KEY)!!
        val url = inputData.getString(URL_KEY)!!
        val imagesUri = if (haveQ())
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        else
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/*")
        }
        val imageUri = applicationContext.contentResolver.insert(imagesUri, contentValues)!!
        applicationContext.contentResolver.openOutputStream(imageUri)?.use {outputStream ->
            Network.unsplashApi.downloadPhoto(url).byteStream().use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        val outputData = workDataOf(IMAGE_URI to imageUri.toString())
        if (AppStatus.isStopped.not())
            return Result.success(outputData)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = imageUri
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 2, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, NotificationChannels.CHANNEL_ID)
            .setContentTitle("Download")
            .setContentText("Download is finished")
            .setSmallIcon(R.drawable.ic_baseline_file_download_done_24)
            .setContentIntent(pendingIntent)
            .build()
        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID, notification)
        return Result.success(outputData)
    }

    companion object {
        const val ID_KEY = "id"
        const val NAME_KEY = "name"
        const val URL_KEY = "url"
        const val DOWNLOAD_WORK_ID = "work id"
        const val NOTIFICATION_ID = 1
        const val IMAGE_URI = "image uri"
    }
}