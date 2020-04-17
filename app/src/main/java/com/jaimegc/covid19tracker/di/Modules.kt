package com.jaimegc.covid19tracker.di

import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.config.ServerApiCovidTrackerConfigBuilder
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTrackerLast
import com.jaimegc.covid19tracker.ui.notifications.WorldViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single {
        CovidTrackerApiClient(get())
    }

    single {
        ServerApiCovidTrackerConfigBuilder().build()
    }
}

val useCaseModule = module {
    single {
        GetCovidTrackerLast(get())
    }
}

val repositoryModule = module {
    single {
        CovidTrackerRepository(get(), get())
    }
}

val viewModelModule = module {
    viewModel {
        WorldViewModel(get())
    }
}

val databaseModule = module {
    single {
        Covid19TrackerDatabase.build(get())
    }
}

val daoModule = module {
    single {
        val database: Covid19TrackerDatabase = get()
        database.covidTrackerTotalDao()
    }

    single {
        val database: Covid19TrackerDatabase = get()
        database.worldTodayStatsDao()
    }

    single {
        val database: Covid19TrackerDatabase = get()
        database.countryTodayStatsDao()
    }
}

val datasourceModule = module {
    single {
        RemoteCovidTrackerDatasource(get())
    }

    single {
        LocalCovidTrackerDatasource(get())
    }
}