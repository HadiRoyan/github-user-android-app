package com.hadroy.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadroy.githubuser.R
import com.hadroy.githubuser.data.remote.response.UserItems
import com.hadroy.githubuser.databinding.ActivityMainBinding
import com.hadroy.githubuser.setting.SettingPreferences
import com.hadroy.githubuser.setting.dataStore
import com.hadroy.githubuser.ui.adapter.UserAdapter
import com.hadroy.githubuser.viewmodel.MainViewModel
import com.hadroy.githubuser.viewmodel.factory.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSearchView()
        val pref = SettingPreferences.getInstance(application.dataStore)
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(pref)).get(MainViewModel::class.java)

        isDarkMode()

        mainViewModel.listUserItems.observe(this) {
            showRecyclerView(it)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            setProgressBar(isLoading)
        }

        onBackPressedDispatcher.addCallback(this, setOnBackListener())
    }

    private fun setupSearchView() {
        with(binding) {
            searchBar.hint = "Search user"
            searchBar.inflateMenu(R.menu.main_menu)
            searchBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.favorite_page -> showFavoritePage()
                    R.id.setting_page -> showSettingPage()
                    else -> false
                }
            }

            searchView.setupWithSearchBar(searchBar)
            searchView.editText
                .setOnEditorActionListener { v, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.getListUsersBySearch(searchView.text.toString())
                    mainViewModel.listUserItems.observe(this@MainActivity) {
                        showRecyclerView(it)
                    }
                    false
                }
        }
    }

    private fun showSettingPage(): Boolean {
        startActivity(Intent(this@MainActivity, SettingActivity::class.java))
        return true
    }

    private fun showFavoritePage(): Boolean {
        startActivity(Intent(this@MainActivity, FavoriteUserActivity::class.java))
        return true
    }

    private fun showRecyclerView(listUserItems: ArrayList<UserItems>) {
        val userAdapter = UserAdapter(listUserItems)

        binding.rvSearchResult.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResult.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserItems) {
                val username = user.login
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USERNAME, username)
                startActivity(intent)
            }
        })
    }

    private fun isDarkMode() {
        mainViewModel.getThemeSettings().observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setOnBackListener(): OnBackPressedCallback {
        val onBackPressedListener = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val mSearchBar = binding.searchBar.text
                if (!mSearchBar.isNullOrEmpty()) {
                    binding.searchBar.text = null
                    mainViewModel.getListUsersBySearch("")
                    mainViewModel.listUserItems.observe(this@MainActivity) {
                        showRecyclerView(it)
                    }
                } else {
                    this@MainActivity.finish()
                }
            }
        }
        return onBackPressedListener
    }
}