package com.skillbox.github.ui.repository_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.data.Repository
import com.skillbox.github.data.RepositoryListRepository

class RepositoryListViewModel: ViewModel() {
    private val repositoryListRepository = RepositoryListRepository()
 private val repositoryListLiveData = MutableLiveData<List<Repository>>()
    val repositoryList: LiveData<List<Repository>>
    get() = repositoryListLiveData

    fun getRepositoryList () {
         repositoryListRepository.getRepositoryList {list ->
             repositoryListLiveData.postValue(list)

         }
    }
}