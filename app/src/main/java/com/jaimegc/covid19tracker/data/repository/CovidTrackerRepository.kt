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

    suspend fun getCovidTrackerByDate(
        date: String
    ): Flow<Either<StateError<DomainError>, State<CovidTracker>>> =
        object : BaseRepository<DomainError, CovidTracker> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, CovidTracker>> =
                local.getCovidTrackerByDate(date)

            override suspend fun fetchFromRemote() {
                remote.getCovidTrackerByDate(date).fold(::Left) { covidTracker -> local.save(covidTracker) }
            }
        }.asFlow(loading = true)

    suspend fun getWorldAllStats(): Flow<Either<StateError<DomainError>, State<ListWorldStats>>> =
        object : BaseRepository<DomainError, ListWorldStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListWorldStats>> =
                local.getWorldAllStats()
        }.asFlow()

    suspend fun getCountryAllStats(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListCountryOnlyStats>>> =
        object : BaseRepository<DomainError, ListCountryOnlyStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryOnlyStats>> =
                local.getCountryAllStats(idCountry)
        }.asFlow()

    suspend fun getRegionAllStats(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionOnlyStats>>> =
        object : BaseRepository<DomainError, ListRegionOnlyStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionOnlyStats>> =
                local.getRegionAllStats(idCountry, idRegion)
        }.asFlow()

    suspend fun getCountriesStatsOrderByConfirmed(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesStatsOrderByConfirmed()
        }.asFlow()

    suspend fun getRegionsStatsOrderByConfirmed(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionStats>>> =
        object : BaseRepository<DomainError, ListRegionStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionStats>> =
                local.getRegionsStatsOrderByConfirmed(idCountry, date)
        }.asFlow()

    suspend fun getSubRegionsStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionStats>>> =
        object : BaseRepository<DomainError, ListSubRegionStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListSubRegionStats>> =
                local.getSubRegionsStatsOrderByConfirmed(idCountry, idRegion, date)
        }.asFlow()

    suspend fun getRegionsAllStatsOrderByConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAllStatsOrderByConfirmed(idCountry)
        }.asFlow()

    suspend fun getSubRegionsAllStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        object : BaseRepository<DomainError, ListSubRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListSubRegionAndStats>> =
                local.getSubRegionsAllStatsOrderByConfirmed(idCountry, idRegion)
        }.asFlow()

    suspend fun getCountriesAndStatsWithMostConfirmed(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostConfirmed()
        }.asFlow()

    suspend fun getRegionsAndStatsWithMostConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAndStatsWithMostConfirmed(idCountry)
        }.asFlow()

    suspend fun getCountriesAndStatsWithMostDeaths(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostDeaths()
        }.asFlow()

    suspend fun getRegionsAndStatsWithMostDeaths(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAndStatsWithMostDeaths(idCountry)
        }.asFlow()

    suspend fun getCountriesAndStatsWithMostRecovered()
            : Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostRecovered()
        }.asFlow()

    suspend fun getRegionsAndStatsWithMostRecovered(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAndStatsWithMostRecovered(idCountry)
        }.asFlow()

    suspend fun getCountriesAndStatsWithMostOpenCases(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostOpenCases()
        }.asFlow()

    suspend fun getRegionsAndStatsWithMostOpenCases(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAndStatsWithMostOpenCases(idCountry)
        }.asFlow()

    suspend fun getCountries(): Flow<Either<StateError<DomainError>, State<ListCountry>>> =
        object : BaseRepository<DomainError, ListCountry> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountry>> =
                local.getCountries()
        }.asFlow(loading = true)

    suspend fun getRegionsByCountry(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegion>>> =
        object : BaseRepository<DomainError, ListRegion> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegion>> =
                local.getRegionsByCountry(idCountry)
        }.asFlow()

    suspend fun getCountryAndStatsByDate(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<CountryOneStats>>> =
        object : BaseRepository<DomainError, CountryOneStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, CountryOneStats>> =
                local.getCountryAndStatsByDate(idCountry, date)
        }.asFlow()

    suspend fun getRegionAndStatsByDate(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<RegionOneStats>>> =
        object : BaseRepository<DomainError, RegionOneStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, RegionOneStats>> =
                local.getRegionAndStatsByDate(idCountry, idRegion, date)
        }.asFlow()
}