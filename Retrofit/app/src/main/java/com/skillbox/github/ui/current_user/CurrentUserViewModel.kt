package com.skillbox.github.ui.current_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.data.CurrentUserRepository
import com.skillbox.github.data.User

class CurrentUserViewModel: ViewModel() {
    private val currentUserRepository = CurrentUserRepository()
    private val currentUserLiveData = MutableLiveData<User>()
    val currentUser: LiveData<User>
    get() = currentUserLiveData
    fun getCurrentUser () {
          currentUserRepository.getCurrentUser {user ->
              currentUserLiveData.postValue(user)
          }
    }
}