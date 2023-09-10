package com.hadroy.githubuser.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hadroy.githubuser.R
import com.hadroy.githubuser.data.response.DetailUserResponse
import com.hadroy.githubuser.databinding.ActivityDetailUserBinding
import com.hadroy.githubuser.ui.adapter.SectionsPagerAdapter
import com.hadroy.githubuser.viewmodel.DetailUserViewModel

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding

    private val detailUserViewModel by viewModels<DetailUserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        detailUserViewModel.getDetailUser(username.toString())

        detailUserViewModel.detailUser.observe(this) { detailUser ->
            setData(detailUser)
        }

        setupTabLayout(username!!)

    }


    private fun setData(data: DetailUserResponse?) {
        binding.tvFullName.text = data?.name
        binding.tvUsername.text = data?.login

        binding.tvFollowers.text = data?.followers.toString()
        binding.tvFollowing.text = data?.following.toString()
        binding.tvRepo.text = data?.publicRepos.toString()
        Glide.with(binding.root)
            .load(data?.avatarUrl)
            .circleCrop()
            .into(binding.imgProfile)
    }

    private fun setupTabLayout(username: String) {
        val sectionPagerAdapter = SectionsPagerAdapter(this@DetailUserActivity)
        val viewPager: ViewPager2 = binding.viewPager
        sectionPagerAdapter.username = username
        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }
}