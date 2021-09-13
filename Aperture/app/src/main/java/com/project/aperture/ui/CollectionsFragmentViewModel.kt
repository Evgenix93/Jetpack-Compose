package com.project.aperture.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.aperture.data.Collection
import com.project.aperture.data.CollectionDB
import com.project.aperture.data.PhotosRepository
import com.project.aperture.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class CollectionsFragmentViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = PhotosRepository(app)
    private val collectionsMutableLiveData = MutableLiveData<List<Collection>>()
    val collectionsLiveData: LiveData<List<Collection>> = collectionsMutableLiveData
    private val collectionsDBMutableLiveData = MutableLiveData<List<CollectionDB>>()
    val collectionsDBLiveData: LiveData<List<CollectionDB>> = collectionsDBMutableLiveData
    private val noConnectionLiveEvent =
        SingleLiveEvent<Boolean>()
    val noConnectionLiveData: LiveData<Boolean> = noConnectionLiveEvent
    private var page = 1

    fun getCollections() {
        viewModelScope.launch {
            val collections = repository.getCollections(page)
            if (collections.isNotEmpty()){
                if (page == 1){
                    collectionsMutableLiveData.postValue(collections)
                    repository.saveCollectionsDB(collections)
                }else
                    collectionsMutableLiveData.postValue(collectionsMutableLiveData.value.orEmpty() + collections)
                page++
            }
            else {
                collectionsMutableLiveData.postValue(emptyList())
                val collectionsDB = repository.getCollectionsDB()
                    collectionsDBMutableLiveData.postValue(collectionsDB)
                noConnectionLiveEvent.postValue(collectionsDB.isEmpty())
            }
        }
    }

    fun clearPage(){
        page = 1
    }
}