package com.example.movietracker.presentation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.movietracker.R
import com.example.movietracker.databinding.ActivityMainBinding
import com.example.movietracker.presentation.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for the Movie Tracker app.
 *
 * This activity serves as the entry point and container for the app's fragments.
 * It follows the Single Activity pattern with multiple fragments.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var navController: NavController

    override fun inflateViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun setupUI() {
        // Additional UI setup can be done here
    }

    override fun observeViewModel() {
        // No ViewModel to observe at the activity level
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}