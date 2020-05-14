package com.jaimegc.covid19tracker.domain.usecase

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.CountryOneStats
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.ListCountryStats
import com.jaimegc.covid19tracker.domain.model.ListRegionStats
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.flow.Flow

class GetRegionStats(
    private val repository: CovidTrackerRepository
) {

    suspend fun getRegionsStatsOrderByConfirmed(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionStats>>> =
        repository.getRegionsStatsOrderByConfirmed(idCountry, date)

    suspend fun getCountriesAndStatsWithMostConfirmed(
    ): Flow<Either<StateError<DomainError>, State<ListCountryStats>>> =
        repository.getCountriesAndStatsWithMostConfirmed()

    suspend fun getCountriesAndStatsWithMostDeaths(
    ): Flow<Either<StateError<DomainError>, State<ListCountryStats>>> =
        repository.getCountriesAndStatsWithMostDeaths()

    suspend fun getCountriesAndStatsWithMostRecovered(
    ): Flow<Either<StateError<DomainError>, State<ListCountryStats>>> =
        repository.getCountriesAndStatsWithMostRecovered()

    suspend fun getCountriesAndStatsWithMostOpenCases(
    ): Flow<Either<StateError<DomainError>, State<ListCountryStats>>> =
        repository.getCountriesAndStatsWithMostOpenCases()

    suspend fun getCountryAndStatsByIdDate(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<CountryOneStats>>> =
        repository.getCountryAndStatsByIdDate(idCountry, date)
}