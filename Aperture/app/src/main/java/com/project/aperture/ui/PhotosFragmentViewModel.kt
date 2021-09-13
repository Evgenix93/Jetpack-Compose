package com.project.aperture

import android.app.Application
import androidx.lifecycle.*
import com.project.aperture.data.AuthRepository
import com.project.aperture.data.Photo
import com.project.aperture.data.PhotoDB
import com.project.aperture.data.PhotosRepository
import com.project.aperture.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PhotosFragmentViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = PhotosRepository(app)
    private val authRepository = AuthRepository(app)
    private val photosMutableLiveData = MutableLiveData<List<Photo>>()
    val photosLiveData: LiveData <List<Photo>> = photosMutableLiveData
    private val photosDBMutableLiveData = MutableLiveData<List<PhotoDB>>()
    val photosDBLiveData: LiveData<List<PhotoDB>> = photosDBMutableLiveData
    private val startFragmentLiveEvent =
        SingleLiveEvent<Unit>()
    val startFragmentLiveData: LiveData<Unit> = startFragmentLiveEvent
    private val photoMutableLiveData =
        MutableLiveData<Pair<Photo, Int>?>()
    val photoLiveData: LiveData<Pair<Photo, Int>?> = photoMutableLiveData
    private val noConnectionLiveEvent =
        SingleLiveEvent<Boolean>()
    val noConnectionLiveData: LiveData<Boolean> = noConnectionLiveEvent
    private var coroutine: Job? = null
    private var page = 1

    fun getFirstPhotos() {
        if (authRepository.getAccessToken() == null)
            startFragmentLiveEvent.postValue(Unit)
        else
            getPhotos()
    }

    fun getPhotos() {
            viewModelScope.launch {
                photoMutableLiveData.postValue(null)
                val photos = repository.getPhotos(page)
                if (photos.isNotEmpty()) {
                    if (page == 1) {
                        photosMutableLiveData.postValue(photos)
                        repository.savePhotosDB(photos, "")
                    }
                    else
                        photosMutableLiveData.postValue(photosMutableLiveData.value.orEmpty() + photos)
                    page++
                } else {
                    photosMutableLiveData.postValue(emptyList())
                    val photosDB = repository.getPhotosDB("")
                        photosDBMutableLiveData.postValue(photosDB)
                     noConnectionLiveEvent.postValue(photosDB.isEmpty())
                }
            }
    }

    fun likePhoto(position: Int, likedByUser: Boolean) {
        viewModelScope.launch {
            var photo = photosMutableLiveData.value?.get(position)
            if (photo != null) {
                photo = repository.likePhoto(photo.id, likedByUser)
                if (photo != null)
                    photoMutableLiveData.postValue(photo!! to position)
            }
        }
    }

    fun searchPhotos(flow: Flow<String>) {
        coroutine = flow.debounce(1000)
            .mapLatest {
                if (it.isNotEmpty())
                    repository.searchPhotos(it)
                else
                    repository.getPhotos(1)
            }
            .onEach { photosMutableLiveData.postValue(it) }
            .launchIn(viewModelScope)
    }

    fun cancelCoroutine() {
        coroutine?.cancel()
    }

    fun clearPage(){
        page = 1
    }
}