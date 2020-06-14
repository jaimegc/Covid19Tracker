package com.jaimegc.covid19tracker.ui.base

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import org.koin.core.KoinComponent

abstract class BaseActivity : AppCompatActivity(), KoinComponent {

    // We should adapt the UI for all font scales :[
    override fun attachBaseContext(newBase: Context) {
        val override = Configuration(newBase.resources.configuration)
        override.fontScale = 1.0f
        applyOverrideConfiguration(override)
        super.attachBaseContext(newBase)
    }
}