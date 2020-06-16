package com.jaimegc.covid19tracker.di

import androidx.lifecycle.SavedStateHandle
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.config.ServerApiCovidTrackerConfigBuilder
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.preference.CountryPreferences
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.data.room.Covid19TrackerDatabase
import com.jaimegc.covid19tracker.domain.usecase.GetDates
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.domain.usecase.GetRegion
import com.jaimegc.covid19tracker.domain.usecase.GetRegionStats
import com.jaimegc.covid19tracker.domain.usecase.GetSubRegionStats
import com.jaimegc.covid19tracker.domain.usecase.GetWorldAndCountries
import com.jaimegc.covid19tracker.domain.usecase.GetWorldStats
import com.jaimegc.covid19tracker.ui.country.CountryViewModel
import com.jaimegc.covid19tracker.ui.home.MainViewModel
import com.jaimegc.covid19tracker.ui.world.WorldViewModel
import com.jaimegc.covid19tracker.utils.FileUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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

@ExperimentalCoroutinesApi
val useCaseModule = module {
    single {
        GetCovidTracker(get())
    }

    single {
        GetDates(get())
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

@ExperimentalCoroutinesApi
val repositoryModule = module {
    single {
        CovidTrackerRepository(get(), get(), get())
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
val datasourceModule = module {
    single {
        RemoteCovidTrackerDatasource(get(), get())
    }

    single {
        LocalCovidTrackerDatasource(get(), get(), get(), get(), get(), get(), get())
    }
}

val preferenceModule = module {
    single {
        CovidTrackerPreferences(get())
    }

    single {
        CountryPreferences(get())
    }
}

val othersModule = module {
    single {
        FileUtils(get())
    }
}