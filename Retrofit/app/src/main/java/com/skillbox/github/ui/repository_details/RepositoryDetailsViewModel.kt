package com.skillbox.github.ui.repository_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.data.RepositoryDetailsRepository

class RepositoryDetailsViewModel: ViewModel() {
    private val repository = RepositoryDetailsRepository()
    private val isCheckedLiveData = MutableLiveData<Boolean>()
    val isChecked: LiveData<Boolean>
    get() = isCheckedLiveData
    fun putStar (owner:String, repo:String) {
        repository.putStar(owner, repo)
    }

    fun removeStar(owner:String, repo:String) {
        repository.removeStar(owner, repo)
    }

    fun checkStar(owner:String, repo:String){
        repository.checkStar(owner, repo) {code ->
            if (code == 204) {
                Log.d("Test", code.toString())
                isCheckedLiveData.postValue(true)
            }else{
            Log.d("Test", code.toString())
            isCheckedLiveData.postValue(false)
        }
        }
    }
}