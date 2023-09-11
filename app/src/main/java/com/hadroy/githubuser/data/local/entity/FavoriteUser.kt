package com.hadroy.githubuser.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_users")
@Parcelize
data class FavoriteUser(

    @PrimaryKey(autoGenerate = false)
    var username: String = "",

    var avatarUrl: String? = null
) : Parcelable
