package com.jaimegc.covid19tracker.domain.usecase

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.CountryOneStats
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.ListCountryAndStats
import com.jaimegc.covid19tracker.domain.model.ListCountryOnlyStats
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class GetCountryStats(
    private val repository: CovidTrackerRepository
) {

    fun getCountryAllStats(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListCountryOnlyStats>>> =
        repository.getCountryAllStats(idCountry)

    fun getCountriesStatsOrderByConfirmed(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        repository.getCountriesStatsOrderByConfirmed()

    fun getCountriesAndStatsWithMostConfirmed(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        repository.getCountriesAndStatsWithMostConfirmed()

    fun getCountriesAndStatsWithMostDeaths(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        repository.getCountriesAndStatsWithMostDeaths()

    fun getCountriesAndStatsWithMostRecovered(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        repository.getCountriesAndStatsWithMostRecovered()

    fun getCountriesAndStatsWithMostOpenCases(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        repository.getCountriesAndStatsWithMostOpenCases()

    fun getCountryAndStatsByDate(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<CountryOneStats>>> =
        repository.getCountryAndStatsByDate(idCountry, date)
}