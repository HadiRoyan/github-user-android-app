package com.hadroy.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadroy.githubuser.R
import com.hadroy.githubuser.data.local.entity.FavoriteUser
import com.hadroy.githubuser.databinding.ActivityFavoriteUserBinding
import com.hadroy.githubuser.ui.adapter.FavoriteAdapter
import com.hadroy.githubuser.viewmodel.FavoriteUserViewModel
import com.hadroy.githubuser.viewmodel.factory.FavoriteUserViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    private lateinit var viewModel: FavoriteUserViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = obtainViewModel(this@FavoriteUserActivity)
        setTopBar()

        adapter = FavoriteAdapter()
        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(user: FavoriteUser) {
                val intent = Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.username)
                startActivity(intent)
            }
        })
        binding.rvFavoriteUser.layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUser.adapter = adapter

        observeData()
    }

    private fun observeData() {
        viewModel.getAllFavoriteUsers().observe(this@FavoriteUserActivity) { listFavoriteUsers ->
            binding.progressBar.visibility = View.VISIBLE
            if (listFavoriteUsers != null) {
                adapter.setListFavoriteUsers(listFavoriteUsers)
            }
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = FavoriteUserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteUserViewModel::class.java)
    }

    private fun setTopBar() {
        binding.topAppBar.setNavigationOnClickListener { v ->
            onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.setting_page -> {
                    startActivity(Intent(this@FavoriteUserActivity, SettingActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    override fun onResume() {
        observeData()
        super.onResume()
    }
}