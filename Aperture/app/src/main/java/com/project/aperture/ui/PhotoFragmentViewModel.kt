package com.project.aperture.ui

import android.app.Application
import androidx.lifecycle.*
import com.project.aperture.data.Photo
import com.project.aperture.data.PhotosRepository
import com.project.aperture.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class PhotoFragmentViewModel(app: Application): AndroidViewModel(app) {
    private val repository = PhotosRepository(app)
    private val photoLiveEvent =
        SingleLiveEvent<Photo?>()
    val photoLiveData: LiveData<Photo?> = photoLiveEvent
    private val internetConnectionLiveEvent =
        SingleLiveEvent<Boolean>()
    val internetConnectionLiveData: LiveData<Boolean> = internetConnectionLiveEvent
    private val showMapLiveEvent =
        SingleLiveEvent<Map<String, Float>>()
    val showMapLiveData: LiveData<Map<String, Float>> = showMapLiveEvent

    fun getPhoto(id: String){
       viewModelScope.launch {
           val photo = repository.getPhoto(id)
               photoLiveEvent.postValue(photo)
       }
    }

    fun likePhoto() {
        viewModelScope.launch {
            var photo = photoLiveEvent.value
            if (photo != null) {
                photo = repository.likePhoto(photo.id, photo.likedByUser)
                if (photo != null)
                    photoLiveEvent.postValue(repository.getPhoto(photo.id))
            }
        }
    }
    fun launchWorkManager(){
        val photo = photoLiveEvent.value
        if (photo != null)
        repository.launchWorkManager(photo.id, "${photo.id}.jpg" , photo.urls["raw"]!!)
    }

    fun checkInternet(id: String) {
        viewModelScope.launch {
            if (repository.getPhoto(id) == null)
                internetConnectionLiveEvent.postValue(false)
        }
    }

    fun trackDownloads(id: String){
      viewModelScope.launch {
          repository.trackDownloads(id)
          photoLiveEvent.postValue(repository.getPhoto(id))
      }
    }

    fun showMap(){
        val location = photoLiveEvent.value?.location
        if (location != null){
             showMapLiveEvent.postValue(location.position)
        }
    }
}