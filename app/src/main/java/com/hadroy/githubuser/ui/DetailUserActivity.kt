package com.hadroy.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hadroy.githubuser.R
import com.hadroy.githubuser.data.local.entity.FavoriteUser
import com.hadroy.githubuser.data.remote.response.DetailUserResponse
import com.hadroy.githubuser.databinding.ActivityDetailUserBinding
import com.hadroy.githubuser.ui.adapter.SectionsPagerAdapter
import com.hadroy.githubuser.viewmodel.DetailUserViewModel
import com.hadroy.githubuser.viewmodel.factory.DetailUserViewModelFactory

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel by viewModels<DetailUserViewModel> {
        DetailUserViewModelFactory.getInstance(this.application)
    }

    private var isFavoriteUser: Boolean = false
    private var avatarUrlCurrent: String? = null

    var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTopBar()
        username = intent.getStringExtra(EXTRA_USERNAME)
        viewModel.getDetailUser(username.toString())

        viewModel.detailUser.observe(this) { detailUser ->
            avatarUrlCurrent = detailUser?.avatarUrl
            setData(detailUser)
        }


        viewModel.getFavoriteUserByUsername(username.toString()).observe(this) { favoriteUser ->
            if (favoriteUser != null) {
                isFavoriteUser = true
                binding.fabAdd.setImageResource(R.drawable.ic_favorite)
            } else {
                isFavoriteUser = false
                binding.fabAdd.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        setFAB(username)
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

    private fun setTopBar() {
        binding.topAppBar.setNavigationOnClickListener { v ->
            Log.d("Navigation", "onClick: ${v.id}")
            onBackPressed()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.setting_page -> {
                    startActivity(Intent(this@DetailUserActivity, SettingActivity::class.java))
                    true
                }

                R.id.share -> {
                    val userAccountLink = "https://github.com/$username"
                    val share = Intent.createChooser(Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            userAccountLink
                        )
                        putExtra(Intent.EXTRA_TITLE, "GitHub $username previews")
                    }, "Share")
                    startActivity(share)
                    true
                }

                else -> false
            }
        }
    }

    private fun setFAB(username: String?) {
        binding.fabAdd.setOnClickListener {
            val favoriteUser = FavoriteUser(username.toString(), avatarUrlCurrent)
            if (!isFavoriteUser) {
                viewModel.insert(favoriteUser)
            } else {
                viewModel.delete(favoriteUser)
            }
        }
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