package com.jaimegc.covid19tracker.di

import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.config.ServerApiCovidTrackerConfigBuilder
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTrackerLast
import com.jaimegc.covid19tracker.ui.notifications.NotificationsViewModel
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
        CovidTrackerRepository(get())
    }
}

val viewModelModule = module {
    viewModel {
        NotificationsViewModel(get())
    }
}