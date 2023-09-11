package com.hadroy.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadroy.githubuser.data.FavoriteRepository
import com.hadroy.githubuser.data.local.entity.FavoriteUser

class FavoriteUserViewModel(application: Application) : ViewModel() {


    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> =
        favoriteRepository.getAllFavoriteUsers()
}