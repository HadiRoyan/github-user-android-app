package com.hadroy.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.hadroy.githubuser.data.local.entity.FavoriteUser
import com.hadroy.githubuser.data.local.room.FavoriteDatabase
import com.hadroy.githubuser.data.local.room.FavoriteUsersDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {

    private val favoriteUsersDao: FavoriteUsersDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getInstance(application)
        favoriteUsersDao = db.favoriteUsersDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = favoriteUsersDao.getAllFavoriteUsers()

    fun insert(user: FavoriteUser) {
        executorService.execute { favoriteUsersDao.insert(user) }
    }

    fun delete(user: FavoriteUser) {
        executorService.execute { favoriteUsersDao.delete(user) }
    }

    fun getFavoriteUser(username: String): LiveData<FavoriteUser> =
        favoriteUsersDao.getFavoriteUserByUsername(username)

}
