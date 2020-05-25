package com.jaimegc.covid19tracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.extensions.Coroutines
import com.jaimegc.covid19tracker.common.extensions.hide
import com.jaimegc.covid19tracker.databinding.ActivityMainBinding
import com.jaimegc.covid19tracker.databinding.LoadingDatabaseBinding
import com.jaimegc.covid19tracker.ui.base.KeepStateNavigator
import com.jaimegc.covid19tracker.utils.FileUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingBinding: LoadingDatabaseBinding
    private lateinit var navigator: KeepStateNavigator
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingBinding = LoadingDatabaseBinding.bind(binding.root)

        Coroutines.ioMain({ FileUtils(this).initDatabase() }) {
            initializeBottomNavigationBar()
        }
    }

    private fun initializeBottomNavigationBar() {
        navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        navigator =
            KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider += navigator
        navController.setGraph(R.navigation.mobile_navigation)

        binding.navView.setupWithNavController(navController)
        loadingBinding.loadingDatabaseLayout.hide()
    }

    override fun onBackPressed() {
        if (binding.navView.selectedItemId == R.id.navigation_country) {
            super.onBackPressed()
        } else {
            binding.navView.selectedItemId = R.id.navigation_country
        }
    }
}
