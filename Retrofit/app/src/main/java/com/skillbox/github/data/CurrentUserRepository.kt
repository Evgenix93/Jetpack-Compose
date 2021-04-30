package com.skillbox.github.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentUserRepository {
    fun getCurrentUser (onComplete: (User?) -> Unit) {
        Network.githubApi.currentUser().enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                onComplete(response.body())
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }

        })
    }

}