package com.skillbox.github.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryDetailsRepository {

    fun putStar (owner:String, repo:String) {
        Network.githubApi.putStar(owner, repo).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun removeStar(owner:String, repo:String) {
        Network.githubApi.removeStar(owner, repo).enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun checkStar(owner:String, repo:String, onComplete: (Int) -> Unit) {
        Network.githubApi.checkStar(owner, repo).enqueue(object: Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                onComplete(response.code())
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                TODO("Not yet implemented")
            }


        })
    }
}