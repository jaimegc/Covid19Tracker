package com.jaimegc.covid19tracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.databinding.ActivityMainBinding
import com.jaimegc.covid19tracker.ui.base.KeepStateNavigator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigator: KeepStateNavigator
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeBottomNavigationBar()
    }

    private fun initializeBottomNavigationBar() {
        navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
        navigator =
            KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
        navController.navigatorProvider += navigator
        navController.setGraph(R.navigation.mobile_navigation)

        navigator.popBackStack()

        binding.navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if (binding.navView.selectedItemId == R.id.navigation_country) {
            super.onBackPressed()
        } else {
            binding.navView.selectedItemId = R.id.navigation_country
        }
    }
}
