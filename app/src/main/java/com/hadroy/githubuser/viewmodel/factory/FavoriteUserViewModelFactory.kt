package com.hadroy.githubuser.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hadroy.githubuser.viewmodel.FavoriteUserViewModel

class FavoriteUserViewModelFactory private constructor(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)) {
            return FavoriteUserViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: FavoriteUserViewModelFactory? = null
        fun getInstance(application: Application): FavoriteUserViewModelFactory =
            instance ?: synchronized(this) {
                instance
                    ?: FavoriteUserViewModelFactory(application)
            }.also { instance = it }
    }
}