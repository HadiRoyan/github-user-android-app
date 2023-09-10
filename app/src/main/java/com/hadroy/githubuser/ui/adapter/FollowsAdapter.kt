package com.hadroy.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hadroy.githubuser.data.response.FollowsResponseItem
import com.hadroy.githubuser.databinding.ItemSearchUserBinding

class FollowsAdapter(private var listFollows: ArrayList<FollowsResponseItem?>) :
    RecyclerView.Adapter<FollowsAdapter.FollowViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }


    class FollowViewHolder(val binding: ItemSearchUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: FollowsResponseItem) {
            binding.tvItemName.text = user.login
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .circleCrop()
                .into(binding.imgProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val binding =
            ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowViewHolder(binding);
    }

    override fun getItemCount(): Int {
        return listFollows.size
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(listFollows[position]!!)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listFollows[position]!!) }
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: FollowsResponseItem)
    }
}