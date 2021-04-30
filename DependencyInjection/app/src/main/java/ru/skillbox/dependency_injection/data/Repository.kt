package ru.skillbox.dependency_injection.data

import android.content.ContentUris
import android.content.ContentValues
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.skillbox.dependency_injection.utils.haveQ

interface Repository {

    fun observeImages(onChange: () -> Unit)

    fun unregisterObserver()

    suspend fun getImages(): List<Image>

    suspend fun saveImage(name: String, url: String)

    suspend fun deleteImage(id: Long)
}