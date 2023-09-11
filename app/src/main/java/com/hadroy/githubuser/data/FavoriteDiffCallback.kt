package com.hadroy.githubuser.data

import androidx.recyclerview.widget.DiffUtil
import com.hadroy.githubuser.data.local.entity.FavoriteUser

class FavoriteDiffCallback(
    private val oldUserList: List<FavoriteUser>,
    private val newUserList: List<FavoriteUser>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldUserList.size
    }

    override fun getNewListSize(): Int {
        return newUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUserList[oldItemPosition].username == newUserList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldUserList[oldItemPosition]
        val newUser = newUserList[newItemPosition]

        return oldUser.username == newUser.username && oldUser.avatarUrl == newUser.avatarUrl
    }

    companion object {
        private const val TAG = "FavoriteDiffCallback"
    }
}