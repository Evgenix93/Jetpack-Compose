package com.skillbox.github.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryListRepository {
    fun getRepositoryList (onComplete: (List<Repository>?) -> Unit) {
        Network.githubApi.getRepositoryList().enqueue(object: Callback<List<Repository>>{
            override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>) {
                onComplete(response.body())
            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}