package com.jaimegc.covid19tracker

import android.app.Application
import com.jaimegc.covid19tracker.di.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@FlowPreview
@ExperimentalCoroutinesApi
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