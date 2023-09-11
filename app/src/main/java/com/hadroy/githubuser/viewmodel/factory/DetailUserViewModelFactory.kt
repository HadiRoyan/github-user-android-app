package com.hadroy.githubuser.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hadroy.githubuser.viewmodel.DetailUserViewModel

class DetailUserViewModelFactory private constructor(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailUserViewModelFactory? = null
        fun getInstance(application: Application): DetailUserViewModelFactory =
            instance ?: synchronized(this) {
                instance
                    ?: DetailUserViewModelFactory(application)
            }.also { instance = it }
    }
}