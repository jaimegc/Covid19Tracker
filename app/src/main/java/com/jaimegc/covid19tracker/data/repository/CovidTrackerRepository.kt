package com.jaimegc.covid19tracker.data.repository

import arrow.core.Either
import arrow.core.Left
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.Dispatchers
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
                remote.getCovidTrackerByDate(date).fold(::Left) { covidTracker ->
                    local.save(covidTracker)
                }
            }
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getWorldAllStats(): Flow<Either<StateError<DomainError>, State<ListWorldStats>>> =
        object : BaseRepository<DomainError, ListWorldStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListWorldStats>> =
                local.getWorldAllStats()
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getCountryAllStats(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListCountryOnlyStats>>> =
        object : BaseRepository<DomainError, ListCountryOnlyStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryOnlyStats>> =
                local.getCountryAllStats(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getRegionAllStats(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionOnlyStats>>> =
        object : BaseRepository<DomainError, ListRegionOnlyStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionOnlyStats>> =
                local.getRegionAllStats(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getCountriesStatsOrderByConfirmed(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesStatsOrderByConfirmed()
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getRegionsStatsOrderByConfirmed(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionStats>>> =
        object : BaseRepository<DomainError, ListRegionStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionStats>> =
                local.getRegionsStatsOrderByConfirmed(idCountry, date)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getSubRegionsStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionStats>>> =
        object : BaseRepository<DomainError, ListSubRegionStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListSubRegionStats>> =
                local.getSubRegionsStatsOrderByConfirmed(idCountry, idRegion, date)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getRegionsAllStatsOrderByConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAllStatsOrderByConfirmed(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getSubRegionsAllStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        object : BaseRepository<DomainError, ListSubRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListSubRegionAndStats>> =
                local.getSubRegionsAllStatsOrderByConfirmed(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getCountriesAndStatsWithMostConfirmed(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostConfirmed()
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getRegionsAndStatsWithMostConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAndStatsWithMostConfirmed(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getSubRegionsAndStatsWithMostConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        object : BaseRepository<DomainError, ListSubRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListSubRegionAndStats>> =
                local.getSubRegionsAndStatsWithMostConfirmed(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getCountriesAndStatsWithMostDeaths(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostDeaths()
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getRegionsAndStatsWithMostDeaths(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAndStatsWithMostDeaths(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getSubRegionsAndStatsWithMostDeaths(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        object : BaseRepository<DomainError, ListSubRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListSubRegionAndStats>> =
                local.getSubRegionsAndStatsWithMostDeaths(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getCountriesAndStatsWithMostRecovered()
            : Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostRecovered()
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getRegionsAndStatsWithMostRecovered(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAndStatsWithMostRecovered(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getSubRegionsAndStatsWithMostRecovered(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        object : BaseRepository<DomainError, ListSubRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListSubRegionAndStats>> =
                local.getSubRegionsAndStatsWithMostRecovered(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getCountriesAndStatsWithMostOpenCases(
    ): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostOpenCases()
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getRegionsAndStatsWithMostOpenCases(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAndStatsWithMostOpenCases(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getSubRegionsAndStatsWithMostOpenCases(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        object : BaseRepository<DomainError, ListSubRegionAndStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListSubRegionAndStats>> =
                local.getSubRegionsAndStatsWithMostOpenCases(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getCountries(): Flow<Either<StateError<DomainError>, State<ListCountry>>> =
        object : BaseRepository<DomainError, ListCountry> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListCountry>> =
                local.getCountries()
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getRegionsByCountry(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegion>>> =
        object : BaseRepository<DomainError, ListRegion> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, ListRegion>> =
                local.getRegionsByCountry(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getCountryAndStatsByDate(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<CountryOneStats>>> =
        object : BaseRepository<DomainError, CountryOneStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, CountryOneStats>> =
                local.getCountryAndStatsByDate(idCountry, date)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getRegionAndStatsByDate(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<RegionOneStats>>> =
        object : BaseRepository<DomainError, RegionOneStats> {
            override suspend fun fetchFromLocal(): Flow<Either<DomainError, RegionOneStats>> =
                local.getRegionAndStatsByDate(idCountry, idRegion, date)
        }.asFlow().flowOn(Dispatchers.IO)
}