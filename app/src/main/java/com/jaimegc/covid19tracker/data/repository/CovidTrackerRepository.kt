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

    suspend fun getWorldAllStats(): Flow<Either<StateError<DomainError>, State<List<WorldStats>>>> {
        return object : BaseRepository<DomainError, List<WorldStats>> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, List<WorldStats>>> =
                local.getWorldAllStats()
        }.asFlow()
    }

    suspend fun getCountriesStatsOrderByConfirmed(): Flow<Either<StateError<DomainError>, State<List<CountryListStats>>>> {
        return object : BaseRepository<DomainError, List<CountryListStats>> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, List<CountryListStats>>> =
                local.getCountriesStatsOrderByConfirmed()
        }.asFlow()
    }

    suspend fun getCountriesAndStatsWithMostConfirmed(): Flow<Either<StateError<DomainError>, State<List<CountryListStats>>>> {
        return object : BaseRepository<DomainError, List<CountryListStats>> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, List<CountryListStats>>> =
                local.getCountriesAndStatsWithMostConfirmed()
        }.asFlow()
    }

    suspend fun getCountriesAndStatsWithMostDeaths(): Flow<Either<StateError<DomainError>, State<List<CountryListStats>>>> {
        return object : BaseRepository<DomainError, List<CountryListStats>> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, List<CountryListStats>>> =
                local.getCountriesAndStatsWithMostDeaths()
        }.asFlow()
    }

    suspend fun getCountriesStatsOrderByDeaths(): Flow<Either<StateError<DomainError>, State<List<CountryStats>>>> {
        return object : BaseRepository<DomainError, List<CountryStats>> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, List<CountryStats>>> =
                local.getCountriesStatsOrderByDeaths()
        }.asFlow()
    }
}