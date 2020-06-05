package com.jaimegc.covid19tracker.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.extensions.Coroutines
import com.jaimegc.covid19tracker.common.extensions.hide
import com.jaimegc.covid19tracker.databinding.ActivityMainBinding
import com.jaimegc.covid19tracker.databinding.LoadingDatabaseBinding
import com.jaimegc.covid19tracker.ui.base.KeepStateNavigator
import com.jaimegc.covid19tracker.utils.FileUtils
import com.jaimegc.covid19tracker.worker.UpdateDatabaseWorker
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), KoinComponent {

    private val viewModel: MainViewModel by viewModel()

    private val fileUtils: FileUtils by inject()
    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingBinding: LoadingDatabaseBinding
    private lateinit var navigator: KeepStateNavigator
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingBinding = LoadingDatabaseBinding.bind(binding.root)

        Coroutines.ioMain({ fileUtils.initDatabase() }) {
            initializeBottomNavigationBar()
            viewModel.getCovidTracker()
            initUpdateDatabaseWorker()
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

    private fun initUpdateDatabaseWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<UpdateDatabaseWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            UpdateDatabaseWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null) {
                    // TODO: Implement feedback
                }
            })
    }

    override fun onBackPressed() {
        if (binding.navView.selectedItemId == R.id.navigation_country) {
            super.onBackPressed()
        } else {
            binding.navView.selectedItemId = R.id.navigation_country
        }
    }
}
