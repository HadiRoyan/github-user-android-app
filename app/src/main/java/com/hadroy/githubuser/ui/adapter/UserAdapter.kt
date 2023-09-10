package com.hadroy.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hadroy.githubuser.data.response.UserItems
import com.hadroy.githubuser.databinding.ItemSearchUserBinding

class UserAdapter(private var listUserItems: ArrayList<UserItems>) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(private val binding: ItemSearchUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserItems) {
            binding.tvItemName.text = user.login
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .circleCrop()
                .into(binding.imgProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSearchUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listUserItems.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listUserItems[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUserItems[position]) }
    }


    interface OnItemClickCallback {
        fun onItemClicked(user: UserItems)
    }
}