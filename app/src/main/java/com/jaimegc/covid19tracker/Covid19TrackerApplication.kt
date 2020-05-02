package com.jaimegc.covid19tracker

import android.app.Application
import com.jaimegc.covid19tracker.di.*
import com.jaimegc.covid19tracker.utils.FileUtils
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Covid19TrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                networkModule,
                useCaseModule,
                repositoryModule,
                viewModelModule,
                databaseModule,
                daoModule,
                datasourceModule)
        }

        FileUtils(this).initDatabase()
    }
}