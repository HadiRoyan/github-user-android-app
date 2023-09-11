package com.hadroy.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadroy.githubuser.data.remote.response.FollowsResponseItem
import com.hadroy.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    companion object {
        private const val TAG = "FollowViewModel"
    }

    private var _detailFollowers = MutableLiveData<ArrayList<FollowsResponseItem?>>()
    val detailFollowers: LiveData<ArrayList<FollowsResponseItem?>> = _detailFollowers

    private var _detailFollowing = MutableLiveData<ArrayList<FollowsResponseItem?>>()
    val detailFollowing: LiveData<ArrayList<FollowsResponseItem?>> = _detailFollowing

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)

        client.enqueue(object : Callback<List<FollowsResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowsResponseItem>>,
                response: Response<List<FollowsResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailFollowing.value = response.body()?.let { ArrayList(it) }
                } else {
                    Log.d(TAG, "onResponse: Failed ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowsResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}", t)
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)

        client.enqueue(object : Callback<List<FollowsResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowsResponseItem>>,
                response: Response<List<FollowsResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailFollowers.value = response.body()?.let { ArrayList(it) }
                } else {
                    Log.d(TAG, "onResponse: Failed ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowsResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}", t)
            }
        })
    }
}