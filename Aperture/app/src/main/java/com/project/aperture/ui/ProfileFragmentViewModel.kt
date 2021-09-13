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

class ProfileFragmentViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = PhotosRepository(app)
    private val profileMutableLiveData = MutableLiveData<Profile>()
    val profileLiveData: LiveData<Profile> = profileMutableLiveData
    private val userMutableLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> = userMutableLiveData
    private val profilePhotosMutableLiveData = MutableLiveData<List<Photo>>()
    val profilePhotosLiveData: LiveData<List<Photo>> = profilePhotosMutableLiveData
    private val likedPhotosMutableLiveData = MutableLiveData<List<Photo>>()
    val likedPhotosLiveData: LiveData<List<Photo>> = likedPhotosMutableLiveData
    private val profileCollectionsMutableLiveData = MutableLiveData<List<Collection>>()
    val profileCollectionsLiveData: LiveData<List<Collection>> = profileCollectionsMutableLiveData
    private val showMapLiveEvent =
        SingleLiveEvent<Map<String, Float>>()
    val showMapLiveData: LiveData<Map<String, Float>> = showMapLiveEvent
    private val logoutLiveEvent =
        SingleLiveEvent<Unit>()
    val logoutLiveData: LiveData<Unit> = logoutLiveEvent
    private val noConnectionLiveEvent =
        SingleLiveEvent<Unit>()
    val noConnectionLiveData: LiveData<Unit> = noConnectionLiveEvent
    private var page = 1
    private var listType = 1


    fun getProfile() {
        if (profileMutableLiveData.value == null)
            viewModelScope.launch {
                val profile = repository.getProfile()
                if (profile != null) {
                    profileMutableLiveData.postValue(profile!!)
                    val user = repository.getUser(profile.userName)
                    if (user != null)
                        userMutableLiveData.postValue(user!!)
                } else
                    noConnectionLiveEvent.postValue(Unit)
            }
    }

    fun getProfilePhotos() {
        viewModelScope.launch {
            val profilePhotos =
                repository.getProfilePhotos(profileMutableLiveData.value?.userName, page)
            if (page == 1)
                profilePhotosMutableLiveData.postValue(profilePhotos)
            else
                profilePhotosMutableLiveData.postValue(profilePhotosMutableLiveData.value.orEmpty() + profilePhotos)
            if (profilePhotos.isNotEmpty())
                page++
        }
    }

    fun getLikedPhotos() {
        viewModelScope.launch {
            val likedPhotos =
                repository.getLikedPhotos(profileMutableLiveData.value?.userName, page)
            if (page == 1)
                likedPhotosMutableLiveData.postValue(likedPhotos)
            else
                likedPhotosMutableLiveData.postValue(likedPhotosMutableLiveData.value.orEmpty() + likedPhotos)
            if (likedPhotos.isNotEmpty())
                page++
        }
    }

    fun getProfileCollections() {
        viewModelScope.launch {
            val profileCollections =
                repository.getProfileCollections(profileMutableLiveData.value?.userName, page)
            if (page == 1)
                profileCollectionsMutableLiveData.postValue(profileCollections)
            else
                profileCollectionsMutableLiveData.postValue(profileCollectionsMutableLiveData.value.orEmpty() + profileCollections)
            if (profileCollections.isNotEmpty())
                page++
        }
    }

    fun showMap() {
        val location = profileMutableLiveData.value?.location
        if (location != null) {
            showMapLiveEvent.postValue(location.position)
        }
    }

    fun likePhoto(position: Int, likedByUser: Boolean) {
        viewModelScope.launch {
            var photo: Photo? = likedPhotosMutableLiveData.value!![position]
            photo = repository.likePhoto(photo!!.id, likedByUser)
            if (photo != null)
                likedPhotosMutableLiveData.postValue(
                    repository.getLikedPhotos(
                        profileMutableLiveData.value?.userName,
                        1
                    )
                )
        }
    }

    fun morePhotos() {
        when (listType) {
            1 -> getProfilePhotos()
            2 -> getLikedPhotos()
            3 -> getProfileCollections()
        }
    }

    fun changeListType(type: Int) {
        listType = type
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            logoutLiveEvent.postValue(Unit)
        }
    }

    fun clearPage() {
        page = 1
    }

    fun getListType(): Int {
        return listType
    }
}