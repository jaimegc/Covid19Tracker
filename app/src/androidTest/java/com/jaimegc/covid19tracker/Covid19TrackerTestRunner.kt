package com.jaimegc.covid19tracker

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.jaimegc.covid19tracker.di.daoModule
import com.jaimegc.covid19tracker.di.databaseTestModule
import com.jaimegc.covid19tracker.di.datasourceModule
import com.jaimegc.covid19tracker.di.networkModule
import com.jaimegc.covid19tracker.di.othersModule
import com.jaimegc.covid19tracker.di.preferenceModule
import com.jaimegc.covid19tracker.di.repositoryModule
import com.jaimegc.covid19tracker.di.useCaseModule
import com.jaimegc.covid19tracker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class Covid19TrackerTestRunner : AndroidJUnitRunner() {

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return Instrumentation.newApplication(Covid19TrackerApplicationTest::class.java, context)
    }
}

class Covid19TrackerApplicationTest : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(
                networkModule,
                useCaseModule,
                repositoryModule,
                viewModelModule,
                databaseTestModule,
                daoModule,
                datasourceModule,
                preferenceModule,
                othersModule
            )
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}