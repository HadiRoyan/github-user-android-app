package com.hadroy.githubuser.ui

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.hadroy.githubuser.databinding.ActivitySettingBinding
import com.hadroy.githubuser.setting.SettingPreferences
import com.hadroy.githubuser.setting.dataStore
import com.hadroy.githubuser.viewmodel.SettingViewModel
import com.hadroy.githubuser.viewmodel.factory.SettingViewModelFactory

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTopBar()

        val switchTheme = binding.switchTheme

        val pref = SettingPreferences.getInstance(application.dataStore)

        val viewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))
                .get(SettingViewModel::class.java)

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setTopBar() {
        binding.topAppBar.setNavigationOnClickListener { _ ->
            onBackPressed()
        }
    }
}