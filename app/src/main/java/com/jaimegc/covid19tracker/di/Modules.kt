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
import com.jaimegc.covid19tracker.domain.usecase.AddCovidTracker
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
import com.jaimegc.covid19tracker.util.FileUtils
import org.koin.androidx.viewmodel.dsl.viewModel
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

    single {
        AddCovidTracker(get())
    }
}

val repositoryModule = module {
    single {
        CovidTrackerRepository(get(), get(), get())
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

val databaseTestModule = module {
    single {
        Covid19TrackerDatabase.buildTest(get())
    }
}

val daoModule = module {
    single {
        get<Covid19TrackerDatabase>().covidTrackerDao()
    }

    single {
        get<Covid19TrackerDatabase>().worldStatsDao()
    }

    single {
        get<Covid19TrackerDatabase>().countryStatsDao()
    }

    single {
        get<Covid19TrackerDatabase>().countryDao()
    }

    single {
        get<Covid19TrackerDatabase>().regionDao()
    }

    single {
        get<Covid19TrackerDatabase>().regionStatsDao()
    }

    single {
        get<Covid19TrackerDatabase>().subRegionStatsDao()
    }
}

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