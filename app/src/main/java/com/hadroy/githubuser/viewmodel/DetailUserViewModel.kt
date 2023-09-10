package com.hadroy.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadroy.githubuser.data.response.DetailUserResponse
import com.hadroy.githubuser.data.retorfit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    private var _detailUser = MutableLiveData<DetailUserResponse?>()
    val detailUser: LiveData<DetailUserResponse?> = _detailUser

    fun getDetailUser(username: String) {

        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(
                        TAG,
                        "onResponse: failed to fetch detail user data because ${response.message()}"
                    )
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}", t)
            }

        })
    }
}