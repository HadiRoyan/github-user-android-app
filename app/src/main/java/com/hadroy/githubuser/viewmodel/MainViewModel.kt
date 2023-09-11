package com.hadroy.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hadroy.githubuser.data.remote.response.SearchResponse
import com.hadroy.githubuser.data.remote.response.UserItems
import com.hadroy.githubuser.data.remote.retrofit.ApiConfig
import com.hadroy.githubuser.setting.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private var _listUserItems = MutableLiveData<ArrayList<UserItems>>()
    val listUserItems: LiveData<ArrayList<UserItems>> = _listUserItems

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        initData()
    }

    private fun initData() {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getSearchUser(INIT_USER)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUserItems.value = response.body()?.items?.let { ArrayList(it) }
                } else {
                    Log.e(TAG, "onResponse: request not successful because: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getListUsersBySearch(username: String) {
        val client: Call<SearchResponse> = if (username.isEmpty()) {
            ApiConfig.getApiService().getSearchUser(INIT_USER)
        } else {
            ApiConfig.getApiService().getSearchUser(username)
        }
        _isLoading.value = true

        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUserItems.value = response.body()?.items?.let { ArrayList(it) }
                } else {
                    Log.e(TAG, "onResponse: request not successful because: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val INIT_USER = "arif"
    }
}