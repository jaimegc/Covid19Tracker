package com.jaimegc.covid19tracker.di

import androidx.lifecycle.SavedStateHandle
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.config.ServerApiCovidTrackerConfigBuilder
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.preference.CountryPreferences
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.domain.usecase.*
import com.jaimegc.covid19tracker.ui.country.CountryViewModel
import com.jaimegc.covid19tracker.ui.home.MainViewModel
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
        GetCovidTracker(get())
    }

    single {
        GetWorldAndCountries(get())
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

    single {
        GetRegion(get())
    }

    single {
        GetRegionStats(get())
    }

    single {
        GetSubRegionStats(get())
    }
}

val repositoryModule = module {
    single {
        CovidTrackerRepository(get(), get())
    }
}

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }

    viewModel {
        WorldViewModel(get(), get(), get())
    }

    viewModel {
        CountryViewModel(get(), get(), get(), get(), get())
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

    single {
        val database: Covid19TrackerDatabase = get()
        database.regionStatsDao()
    }

    single {
        val database: Covid19TrackerDatabase = get()
        database.subRegionStatsDao()
    }
}

val datasourceModule = module {
    single {
        RemoteCovidTrackerDatasource(get())
    }

    single {
        LocalCovidTrackerDatasource(get(), get(), get(), get(), get(), get(), get())
    }
}

val preferenceModule = module {
    single {
        CountryPreferences(get())
    }
}