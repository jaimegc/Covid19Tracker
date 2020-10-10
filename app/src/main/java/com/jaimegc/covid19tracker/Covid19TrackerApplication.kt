package com.jaimegc.covid19tracker

import android.app.Application
import com.jaimegc.covid19tracker.di.daoModule
import com.jaimegc.covid19tracker.di.databaseModule
import com.jaimegc.covid19tracker.di.datasourceModule
import com.jaimegc.covid19tracker.di.networkModule
import com.jaimegc.covid19tracker.di.othersModule
import com.jaimegc.covid19tracker.di.preferenceModule
import com.jaimegc.covid19tracker.di.repositoryModule
import com.jaimegc.covid19tracker.di.useCaseModule
import com.jaimegc.covid19tracker.di.viewModelModule
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
                datasourceModule,
                preferenceModule,
                othersModule)
        }
    }
}