package com.hadroy.githubuser.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hadroy.githubuser.data.FavoriteDiffCallback
import com.hadroy.githubuser.data.local.entity.FavoriteUser
import com.hadroy.githubuser.databinding.ItemSearchUserBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val listFavoriteUser = ArrayList<FavoriteUser>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setListFavoriteUsers(listUsers: List<FavoriteUser>) {
        val diffCallback = FavoriteDiffCallback(this.listFavoriteUser, listUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUser.clear()
        this.listFavoriteUser.addAll(listUsers)
        Log.d("Favorite Adapter", "set new list")
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: FavoriteUser)
    }

    class FavoriteViewHolder(private val binding: ItemSearchUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FavoriteUser) {
            binding.tvItemName.text = user.username
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .circleCrop()
                .into(binding.imgProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listFavoriteUser.size
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val user = listFavoriteUser[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(user)
        }
    }
}

