package com.jaimegc.covid19tracker.domain.usecase

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.CountryListStats
import com.jaimegc.covid19tracker.domain.model.CountryStats
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.flow.Flow

class GetCountryStats(
    private val repository: CovidTrackerRepository
) {

    suspend fun getCountriesStatsOrderByConfirmed(): Flow<Either<StateError<DomainError>, State<List<CountryListStats>>>> =
        repository.getCountriesStatsOrderByConfirmed()

    suspend fun getCountriesAndStatsWithMostConfirmed(): Flow<Either<StateError<DomainError>, State<List<CountryListStats>>>> =
        repository.getCountriesAndStatsWithMostConfirmed()

    suspend fun getCountriesAndStatsWithMostDeaths(): Flow<Either<StateError<DomainError>, State<List<CountryListStats>>>> =
        repository.getCountriesAndStatsWithMostDeaths()

    suspend fun getCountriesStatsOrderByDeaths(): Flow<Either<StateError<DomainError>, State<List<CountryStats>>>> =
        repository.getCountriesStatsOrderByDeaths()
}