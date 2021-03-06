package com.jaimegc.covid19tracker.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkInfo
import com.jaimegc.covid19tracker.R
import com.jaimegc.covid19tracker.common.KeepStateNavigator
import com.jaimegc.covid19tracker.common.extensions.show
import com.jaimegc.covid19tracker.databinding.ActivityMainBinding
import com.jaimegc.covid19tracker.ui.base.BaseActivity
import com.jaimegc.covid19tracker.ui.dialog.DialogUpdateDatabase
import com.jaimegc.covid19tracker.worker.UpdateDatabaseWorker
import com.jaimegc.covid19tracker.worker.UpdateDatabaseWorker.Companion.UPDATE_TIME_HOURS
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by inject()

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigator: KeepStateNavigator
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeBottomNavigationBar()
        viewModel.getCovidTracker()
        initializeUpdateDatabaseWorker()
    }

    @SuppressLint("RestrictedApi")
    private fun initializeBottomNavigationBar() {
        navController = findNavController(R.id.nav_host_fragment)
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let { navHostFragment ->
            navigator =
                KeepStateNavigator(
                    this,
                    navHostFragment.childFragmentManager,
                    R.id.nav_host_fragment
                )
            navController.navigatorProvider += navigator
            navController.setGraph(R.navigation.mobile_navigation)

            binding.navView.setupWithNavController(navController)
            binding.navView.show()
        }
    }

    private fun initializeUpdateDatabaseWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<UpdateDatabaseWorker>(UPDATE_TIME_HOURS, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            UpdateDatabaseWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

        val dialog = DialogUpdateDatabase.newInstance()

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(
                this,
                { workInfo ->
                    if (workInfo != null) {
                        if (workInfo.state == WorkInfo.State.RUNNING) {
                            when (workInfo.progress.getString(UpdateDatabaseWorker.DATA_PROGRESS)) {
                                this.getString(R.string.worker_start) -> dialog.open(supportFragmentManager)
                                this.getString(R.string.worker_finish) -> dialog.close()
                                else ->
                                    dialog.updateInfoStatus(
                                        workInfo.progress.getString(
                                            UpdateDatabaseWorker.DATA_PROGRESS
                                        ) ?: ""
                                    )
                            }
                         } else if (workInfo.state == WorkInfo.State.ENQUEUED) {
                             dialog.close()
                         }
                    }
                }
            )
    }

    override fun onBackPressed() {
        if (binding.navView.selectedItemId == R.id.navigation_country) {
            super.onBackPressed()
        } else {
            binding.navView.selectedItemId = R.id.navigation_country
        }
    }
}
