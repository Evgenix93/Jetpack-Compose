package com.project.aperture.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.aperture.data.*
import com.project.aperture.data.Collection
import com.project.aperture.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class CollectionFragmentViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = PhotosRepository(app)
    private val collectionMutableLiveData = MutableLiveData<Collection>()
    val collectionLiveData: LiveData<Collection> = collectionMutableLiveData
    private val collectionDBMutableLiveData = MutableLiveData<CollectionDB>()
    val collectionDBLiveData: LiveData<CollectionDB> = collectionDBMutableLiveData
    private val photosMutableLiveData = MutableLiveData<List<Photo>>()
    val photosLiveData: LiveData<List<Photo>> = photosMutableLiveData
    private val photosDBMutableLiveData = MutableLiveData<List<PhotoDB>>()
    val photosDBLiveData: LiveData<List<PhotoDB>> = photosDBMutableLiveData
    private val coverPhotoMutableLiveData = MutableLiveData<Photo>()
    val coverPhotoLiveData: LiveData<Photo> = coverPhotoMutableLiveData
    private val photoLiveEvent =
        SingleLiveEvent<Pair<Photo, Int>>()
    val photoLiveData: LiveData<Pair<Photo, Int>> = photoLiveEvent
    private val noConnectionLiveEvent =
        SingleLiveEvent<Boolean>()
    val noConnectionLiveData: LiveData<Boolean> = noConnectionLiveEvent
    private var page = 1

    fun getCollection(id: String) {
        viewModelScope.launch {
            val collection = repository.getCollection(id)
            if (collection != null)
                collectionMutableLiveData.postValue(collection!!)
            else
                collectionDBMutableLiveData.postValue(repository.getCollectionDB(id))
            getPhotos(id)
        }
    }

    fun getPhotos(collectionId: String) {
        viewModelScope.launch {
            val photos = repository.getCollectionPhotos(collectionId, page)
            if (photos.isNotEmpty()) {
                if (page == 1) {
                    photosMutableLiveData.postValue(photos)
                    repository.savePhotosDB(photos, collectionId)
                } else
                    photosMutableLiveData.postValue(photosMutableLiveData.value.orEmpty() + photos)
                page++
            } else {
                photosMutableLiveData.postValue(emptyList())
                val photosDB = repository.getPhotosDB(collectionId)
                photosDBMutableLiveData.postValue(photosDB)
                noConnectionLiveEvent.postValue(photosDB.isEmpty())
            }
        }
    }

    fun getPhoto(id: String?) {
        viewModelScope.launch {
            coverPhotoMutableLiveData.postValue(repository.getPhoto(id))
        }
    }

    fun likePhoto(position: Int, likedByUser: Boolean) {
        viewModelScope.launch {
            var photo = photosMutableLiveData.value?.get(position)
            if (photo != null) {
                photo = repository.likePhoto(photo.id, likedByUser)
                if (photo != null)
                    photoLiveEvent.postValue(photo!! to position)
            }
        }
    }

    fun clearPage() {
        page = 1
    }
}