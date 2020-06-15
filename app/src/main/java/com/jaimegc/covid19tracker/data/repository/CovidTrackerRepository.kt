package com.jaimegc.covid19tracker.data.repository

import arrow.core.Either
import arrow.core.Left
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class CovidTrackerRepository(
    private val local: LocalCovidTrackerDatasource,
    private val remote: RemoteCovidTrackerDatasource,
    private val covidTrackerPreferences: CovidTrackerPreferences
) {

    fun getCovidTrackerByDate(
        date: String
    ): Flow<Either<StateError<DomainError>, State<CovidTracker>>> =
        object : BaseRepository<DomainError, CovidTracker> {
            override fun fetchFromLocal(): Flow<Either<DomainError, CovidTracker>> =
                local.getCovidTrackerByDate(date)

            override suspend fun fetchFromRemote() {
                remote.getCovidTrackerByDate(date).fold(::Left) { covidTracker ->
                    local.save(covidTracker)
                }
            }
        }.asFlow(policy = CachePolicy.LocalFirst(covidTrackerPreferences.isCacheExpired()))
            .flowOn(Dispatchers.IO)

    fun getWorldAndCountriesByDate(
        date: String
    ): Flow<Either<StateError<DomainError>, State<CovidTracker>>> =
        object : BaseRepository<DomainError, CovidTracker> {
            override fun fetchFromLocal(): Flow<Either<DomainError, CovidTracker>> =
                local.getWorldAndCountriesByDate(date)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getWorldAllStats(): Flow<Either<StateError<DomainError>, State<ListWorldStats>>> =
        object : BaseRepository<DomainError, ListWorldStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListWorldStats>> =
                local.getWorldAllStats()
        }.asFlow().flowOn(Dispatchers.IO)

    fun getCountryAllStats(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListCountryOnlyStats>>> =
        object : BaseRepository<DomainError, ListCountryOnlyStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListCountryOnlyStats>> =
                local.getCountryAllStats(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getRegionAllStats(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionOnlyStats>>> =
        object : BaseRepository<DomainError, ListRegionOnlyStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListRegionOnlyStats>> =
                local.getRegionAllStats(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getCountriesStatsOrderByConfirmed(): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesStatsOrderByConfirmed()
        }.asFlow().flowOn(Dispatchers.IO)

    fun getRegionsStatsOrderByConfirmed(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionStats>>> =
        object : BaseRepository<DomainError, ListRegionStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListRegionStats>> =
                local.getRegionsStatsOrderByConfirmed(idCountry, date)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getSubRegionsStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionStats>>> =
        object : BaseRepository<DomainError, ListSubRegionStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListSubRegionStats>> =
                local.getSubRegionsStatsOrderByConfirmed(idCountry, idRegion, date)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getRegionsAllStatsOrderByConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        object : BaseRepository<DomainError, ListRegionAndStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListRegionAndStats>> =
                local.getRegionsAllStatsOrderByConfirmed(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getSubRegionsAllStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        object : BaseRepository<DomainError, ListSubRegionAndStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListSubRegionAndStats>> =
                local.getSubRegionsAllStatsOrderByConfirmed(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getCountriesAndStatsWithMostConfirmed(): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostConfirmed()
        }.asFlow().flowOn(Dispatchers.IO)

    fun getRegionsAndStatsWithMostConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListRegionAndStats>>>> =
        object : BaseRepository<DomainError, Pair<MenuItemViewType, ListRegionAndStats>> {
            override fun fetchFromLocal(): Flow<Either<DomainError, Pair<MenuItemViewType, ListRegionAndStats>>> =
                local.getRegionsAndStatsWithMostConfirmed(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getSubRegionsAndStatsWithMostConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListSubRegionAndStats>>>> =
        object : BaseRepository<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>> {
            override fun fetchFromLocal(): Flow<Either<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>>> =
                local.getSubRegionsAndStatsWithMostConfirmed(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getCountriesAndStatsWithMostDeaths(): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostDeaths()
        }.asFlow().flowOn(Dispatchers.IO)

    fun getRegionsAndStatsWithMostDeaths(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListRegionAndStats>>>> =
        object : BaseRepository<DomainError, Pair<MenuItemViewType, ListRegionAndStats>> {
            override fun fetchFromLocal(): Flow<Either<DomainError, Pair<MenuItemViewType, ListRegionAndStats>>> =
                local.getRegionsAndStatsWithMostDeaths(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getSubRegionsAndStatsWithMostDeaths(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListSubRegionAndStats>>>> =
        object : BaseRepository<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>> {
            override fun fetchFromLocal(): Flow<Either<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>>> =
                local.getSubRegionsAndStatsWithMostDeaths(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getCountriesAndStatsWithMostRecovered():
            Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostRecovered()
        }.asFlow().flowOn(Dispatchers.IO)

    fun getRegionsAndStatsWithMostRecovered(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListRegionAndStats>>>> =
        object : BaseRepository<DomainError, Pair<MenuItemViewType, ListRegionAndStats>> {
            override fun fetchFromLocal(): Flow<Either<DomainError, Pair<MenuItemViewType, ListRegionAndStats>>> =
                local.getRegionsAndStatsWithMostRecovered(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getSubRegionsAndStatsWithMostRecovered(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListSubRegionAndStats>>>> =
        object : BaseRepository<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>> {
            override fun fetchFromLocal(): Flow<Either<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>>> =
                local.getSubRegionsAndStatsWithMostRecovered(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getCountriesAndStatsWithMostOpenCases(): Flow<Either<StateError<DomainError>, State<ListCountryAndStats>>> =
        object : BaseRepository<DomainError, ListCountryAndStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListCountryAndStats>> =
                local.getCountriesAndStatsWithMostOpenCases()
        }.asFlow().flowOn(Dispatchers.IO)

    fun getRegionsAndStatsWithMostOpenCases(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListRegionAndStats>>>> =
        object : BaseRepository<DomainError, Pair<MenuItemViewType, ListRegionAndStats>> {
            override fun fetchFromLocal(): Flow<Either<DomainError, Pair<MenuItemViewType, ListRegionAndStats>>> =
                local.getRegionsAndStatsWithMostOpenCases(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getSubRegionsAndStatsWithMostOpenCases(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListSubRegionAndStats>>>> =
        object : BaseRepository<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>> {
            override fun fetchFromLocal(): Flow<Either<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>>> =
                local.getSubRegionsAndStatsWithMostOpenCases(idCountry, idRegion)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getCountries(): Flow<Either<StateError<DomainError>, State<ListCountry>>> =
        object : BaseRepository<DomainError, ListCountry> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListCountry>> =
                local.getCountries()
        }.asFlow().flowOn(Dispatchers.IO)

    fun getRegionsByCountry(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegion>>> =
        object : BaseRepository<DomainError, ListRegion> {
            override fun fetchFromLocal(): Flow<Either<DomainError, ListRegion>> =
                local.getRegionsByCountry(idCountry)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getCountryAndStatsByDate(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<CountryOneStats>>> =
        object : BaseRepository<DomainError, CountryOneStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, CountryOneStats>> =
                local.getCountryAndStatsByDate(idCountry, date)
        }.asFlow().flowOn(Dispatchers.IO)

    fun getRegionAndStatsByDate(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<RegionOneStats>>> =
        object : BaseRepository<DomainError, RegionOneStats> {
            override fun fetchFromLocal(): Flow<Either<DomainError, RegionOneStats>> =
                local.getRegionAndStatsByDate(idCountry, idRegion, date)
        }.asFlow().flowOn(Dispatchers.IO)

    suspend fun getAllDates(): Either<DomainError, List<String>> =
        local.getAllDates()
}