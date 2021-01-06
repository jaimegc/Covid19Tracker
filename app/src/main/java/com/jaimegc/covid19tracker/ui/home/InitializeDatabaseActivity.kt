package com.jaimegc.covid19tracker.ui.home

import android.os.Bundle
import com.jaimegc.covid19tracker.common.extensions.ioMain
import com.jaimegc.covid19tracker.common.extensions.openActivity
import com.jaimegc.covid19tracker.databinding.ActivityInitializeDatabaseBinding
import com.jaimegc.covid19tracker.ui.base.BaseActivity
import com.jaimegc.covid19tracker.utils.FileUtils
import org.koin.core.component.inject

class InitializeDatabaseActivity : BaseActivity() {

    private val fileUtils: FileUtils by inject()
    private lateinit var binding: ActivityInitializeDatabaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (fileUtils.databaseExists()) {
            openActivity(MainActivity::class.java)
            finish()
        } else {
            binding = ActivityInitializeDatabaseBinding.inflate(layoutInflater)
            setContentView(binding.root)

            ioMain({ fileUtils.initDatabase() }) {
                openActivity(MainActivity::class.java)
                finish()
            }
        }
    }
}