package com.hadroy.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hadroy.githubuser.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteUsersDao(): FavoriteUsersDao

    companion object {

        @Volatile
        private var instance: FavoriteDatabase? = null

        fun getInstance(context: Context): FavoriteDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDatabase::class.java,
                    "fav_user.db"
                ).build()
            }

    }
}