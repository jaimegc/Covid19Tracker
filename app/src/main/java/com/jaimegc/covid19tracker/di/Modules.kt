package com.jaimegc.covid19tracker.di

import androidx.lifecycle.SavedStateHandle
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.config.ServerApiCovidTrackerConfigBuilder
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTrackerLast
import com.jaimegc.covid19tracker.domain.usecase.GetWorldStats
import com.jaimegc.covid19tracker.ui.country.CountryViewModel
import com.jaimegc.covid19tracker.ui.world.WorldViewModel
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

    single {
        GetWorldStats(get())
    }

    single {
        GetCountryStats(get())
    }

    single {
        GetCountry(get())
    }
}

val repositoryModule = module {
    single {
        CovidTrackerRepository(get(), get())
    }
}

val viewModelModule = module {
    viewModel {
        WorldViewModel(get(), get(), get())
    }

    viewModel {
        CountryViewModel(get(), get())
    }

    single {
        SavedStateHandle()
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
        database.covidTrackerDao()
    }

    single {
        val database: Covid19TrackerDatabase = get()
        database.worldStatsDao()
    }

    single {
        val database: Covid19TrackerDatabase = get()
        database.countryStatsDao()
    }

    single {
        val database: Covid19TrackerDatabase = get()
        database.countryDao()
    }

    single {
        val database: Covid19TrackerDatabase = get()
        database.regionDao()
    }
}

val datasourceModule = module {
    single {
        RemoteCovidTrackerDatasource(get())
    }

    single {
        LocalCovidTrackerDatasource(get(), get(), get(), get(), get())
    }
}