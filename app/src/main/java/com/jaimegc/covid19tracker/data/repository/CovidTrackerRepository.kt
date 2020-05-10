package com.jaimegc.covid19tracker.data.repository

import arrow.core.Either
import arrow.core.Left
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.flow.*

class CovidTrackerRepository(
    private val local: LocalCovidTrackerDatasource,
    private val remote: RemoteCovidTrackerDatasource
) {

    suspend fun getCovidTrackerByDate(date: String): Flow<Either<StateError<DomainError>, State<CovidTracker>>> {
        return object : BaseRepository<DomainError, CovidTracker> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, CovidTracker>> =
                local.getCovidTrackerByDate(date)

            override suspend fun fetchFromRemote() {
                remote.getCovidTrackerByDate(date).fold(::Left) { covidTracker -> local.save(covidTracker) }
            }
        }.asFlow()
    }

    suspend fun getWorldAllStats(): Flow<Either<StateError<DomainError>, State<ListWorldStats>>> {
        return object : BaseRepository<DomainError, ListWorldStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListWorldStats>> =
                local.getWorldAllStats()
        }.asFlow()
    }

    suspend fun getCountriesStatsOrderByConfirmed(): Flow<Either<StateError<DomainError>, State<ListCountryStats>>> {
        return object : BaseRepository<DomainError, ListCountryStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryStats>> =
                local.getCountriesStatsOrderByConfirmed()
        }.asFlow()
    }

    suspend fun getCountriesAndStatsWithMostConfirmed(): Flow<Either<StateError<DomainError>, State<ListCountryStats>>> {
        return object : BaseRepository<DomainError, ListCountryStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryStats>> =
                local.getCountriesAndStatsWithMostConfirmed()
        }.asFlow()
    }

    suspend fun getCountriesAndStatsWithMostDeaths(): Flow<Either<StateError<DomainError>, State<ListCountryStats>>> {
        return object : BaseRepository<DomainError, ListCountryStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryStats>> =
                local.getCountriesAndStatsWithMostDeaths()
        }.asFlow()
    }

    suspend fun getCountriesAndStatsWithMostRecovered(): Flow<Either<StateError<DomainError>, State<ListCountryStats>>> {
        return object : BaseRepository<DomainError, ListCountryStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryStats>> =
                local.getCountriesAndStatsWithMostRecovered()
        }.asFlow()
    }

    suspend fun getCountriesAndStatsWithMostOpenCases(): Flow<Either<StateError<DomainError>, State<ListCountryStats>>> {
        return object : BaseRepository<DomainError, ListCountryStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryStats>> =
                local.getCountriesAndStatsWithMostOpenCases()
        }.asFlow()
    }

    suspend fun getCountries(): Flow<Either<StateError<DomainError>, State<ListCountry>>> {
        return object : BaseRepository<DomainError, ListCountry> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountry>> =
                local.getCountries()
        }.asFlow()
    }

    suspend fun getCountryAndStatsByIdDate(idCountry: String, date: String): Flow<Either<StateError<DomainError>, State<CountryOneStats>>> {
        return object : BaseRepository<DomainError, CountryOneStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, CountryOneStats>> =
                local.getCountryAndStatsByIdDate(idCountry, date)
        }.asFlow()
    }
}