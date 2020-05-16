package com.jaimegc.covid19tracker.domain.usecase

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.*
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

    suspend fun getRegionsAllStatsOrderByConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        repository.getRegionsAllStatsOrderByConfirmed(idCountry)

    suspend fun getCountriesAndStatsWithMostConfirmed(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        repository.getCountriesAndStatsWithMostConfirmed()

    suspend fun getCountriesAndStatsWithMostDeaths(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        repository.getCountriesAndStatsWithMostDeaths()

    suspend fun getCountriesAndStatsWithMostRecovered(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        repository.getCountriesAndStatsWithMostRecovered()

    suspend fun getCountriesAndStatsWithMostOpenCases(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        repository.getCountriesAndStatsWithMostOpenCases()

    suspend fun getCountryAndStatsByIdDate(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<CountryOneStats>>> =
        repository.getCountryAndStatsByIdDate(idCountry, date)
}