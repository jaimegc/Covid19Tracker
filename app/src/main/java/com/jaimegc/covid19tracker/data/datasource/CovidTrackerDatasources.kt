package com.jaimegc.covid19tracker.data.datasource

import arrow.core.Either
import com.jaimegc.covid19tracker.common.extensions.dateToMilliseconds
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.extensions.apiException
import com.jaimegc.covid19tracker.data.api.extensions.mapResponse
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.data.room.daos.*
import com.jaimegc.covid19tracker.data.room.entities.*
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.domain.model.toPojoRegionDomain
import com.jaimegc.covid19tracker.domain.model.toStatsDomain
import com.jaimegc.covid19tracker.domain.model.toPojoCountriesOrdered
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class RemoteCovidTrackerDatasource(
    private val apiClient: CovidTrackerApiClient,
    private val covidTrackerPreferences: CovidTrackerPreferences
) {
    suspend fun getCovidTrackerByDate(date: String): Either<DomainError, CovidTracker> =
        try {
            mapResponse(apiClient.getCovidTrackerByDate(date)) { covidTracker ->
                covidTracker.toDomain(date).also {
                    covidTrackerPreferences.saveTime()
                }
            }
        } catch (exception: Exception) {
            Either.left(exception.apiException())
        }
}

@ExperimentalCoroutinesApi
class LocalCovidTrackerDatasource(
    private val covidTrackerDao: CovidTrackerDao,
    private val worldStatsDao: WorldStatsDao,
    private val countryStatsDao: CountryStatsDao,
    private val countryDao: CountryDao,
    private val regionDao: RegionDao,
    private val regionStatsDao: RegionStatsDao,
    private val subRegionStatsDao: SubRegionStatsDao
) {

    fun getCovidTrackerByDate(date: String): Flow<Either<DomainError, CovidTracker>> =
        when (date.isEmpty()) {
            true ->
                mapEntityValid(covidTrackerDao.getWorldAndCountriesStatsByLastDate()) {
                    covidTrackerPojo -> Pair(covidTrackerPojo.isValid(), covidTrackerPojo.toDomain())
                }
            else ->
                mapEntityValid(covidTrackerDao.getWorldAndCountriesStatsByDate(date.dateToMilliseconds())) {
                    covidTrackerPojo -> Pair(covidTrackerPojo.isValid(), covidTrackerPojo.toDomain())
                }
        }.distinctUntilChanged()

    suspend fun getAllDates(): Either<DomainError, List<String>> =
        Either.right(worldStatsDao.getAllDates())

    fun getWorldAndCountriesByDate(date: String): Flow<Either<DomainError, CovidTracker>> =
        getCovidTrackerByDate(date).distinctUntilChanged()

    fun getWorldAllStats(): Flow<Either<DomainError, ListWorldStats>> =
        mapEntityValid(worldStatsDao.getAll()) { worldEntities ->
            Pair(worldEntities.isNotEmpty(), worldEntities.toDomain())
        }.distinctUntilChanged()

    fun getCountryAllStats(idCountry: String): Flow<Either<DomainError, ListCountryOnlyStats>> =
        mapEntityValid(countryStatsDao.getById(idCountry)) { countryEntities ->
            Pair(countryEntities.isNotEmpty(), countryEntities.toStatsDomain())
        }.distinctUntilChanged()

    fun getRegionAllStats(
        idCountry: String,
        idRegion: String
    ): Flow<Either<DomainError, ListRegionOnlyStats>> =
        mapEntityValid(regionStatsDao.getById(idCountry, idRegion)) { regionEntities ->
            Pair(regionEntities.isNotEmpty(), regionEntities.toStatsDomain())
        }.distinctUntilChanged()

    fun getCountriesStatsOrderByConfirmed(): Flow<Either<DomainError, ListCountryAndStats>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsOrderByConfirmed()) { countriesListStats ->
            Pair(countriesListStats.isNotEmpty(), countriesListStats.toDomain())
        }.distinctUntilChanged()

    fun getRegionsStatsOrderByConfirmed(
        idCountry: String,
        date: String
    ): Flow<Either<DomainError, ListRegionStats>> =
        when (date.isEmpty()) {
            true ->
                mapEntityValid(regionStatsDao.getRegionAndStatsByCountryAndLastDateOrderByConfirmed(
                    idCountry)) { regionsListStats -> Pair(regionsListStats.isNotEmpty(),
                        regionsListStats.toDomain())
                }
            else ->
                mapEntityValid(regionStatsDao.getRegionAndStatsByCountryAndDateOrderByConfirmed(
                    idCountry, date.dateToMilliseconds())) { regionsListStats ->
                        Pair(regionsListStats.isNotEmpty(), regionsListStats.toDomain())
                }
        }.distinctUntilChanged()

    fun getSubRegionsStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<DomainError, ListSubRegionStats>> =
        when (date.isEmpty()) {
            true ->
                mapEntityValid(subRegionStatsDao.getSubRegionAndStatsByCountryAndLastDateOrderByConfirmed(
                    idCountry, idRegion)) { regionsListStats -> Pair(regionsListStats.isNotEmpty(),
                        regionsListStats.toDomain())
                }
            else ->
                mapEntityValid(subRegionStatsDao.getSubRegionAndStatsByCountryAndDateOrderByConfirmed(
                    idCountry, idRegion, date.dateToMilliseconds())) { regionsListStats ->
                        Pair(regionsListStats.isNotEmpty(), regionsListStats.toDomain())
                }
        }.distinctUntilChanged()

    fun getRegionsAllStatsOrderByConfirmed(
        idCountry: String
    ): Flow<Either<DomainError, ListRegionAndStats>> =
        mapEntityValid(regionStatsDao.getRegionAndAllStatsByCountryAndDateOrderByConfirmed(idCountry)) {
            regionsListStats -> Pair(regionsListStats.isNotEmpty(), regionsListStats.toPojoRegionDomain())
        }.distinctUntilChanged()

    fun getSubRegionsAllStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<DomainError, ListSubRegionAndStats>> =
        mapEntityValid(subRegionStatsDao.getSubRegionAndAllStatsByCountryAndDateOrderByConfirmed(idCountry, idRegion)) {
            regionsListStats -> Pair(regionsListStats.isNotEmpty(), regionsListStats.toPojoSubRegionDomain())
        }.distinctUntilChanged()

    fun getCountriesAndStatsWithMostConfirmed(): Flow<Either<DomainError, ListCountryAndStats>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostConfirmed()) { countriesListOneStats ->
            countriesListOneStats.toPojoCountriesOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.toDomain())
            }
        }.distinctUntilChanged()

    fun getRegionsAndStatsWithMostConfirmed(
        idCountry: String
    ): Flow<Either<DomainError, Pair<MenuItemViewType, ListRegionAndStats>>> =
        mapEntityValid(regionStatsDao.getRegionsAndStatsWithMostConfirmed(idCountry)) {
            regionsListOneStats -> regionsListOneStats.toPojoRegionsOrdered().let {
                regionsListStats -> Pair(regionsListStats.isNotEmpty(),
                    Pair(MenuItemViewType.LineChartMostConfirmed, regionsListStats.toDomain()))
            }
        }.distinctUntilChanged()

    fun getSubRegionsAndStatsWithMostConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>>> =
        mapEntityMenuValid(subRegionStatsDao.getSubRegionsAndStatsWithMostConfirmed(idCountry, idRegion)) {
            subRegionsListOneStats -> subRegionsListOneStats.toPojoSubRegionsOrdered().let {
                subRegionsListStats -> Pair(subRegionsListStats.isNotEmpty(),
                    Pair(MenuItemViewType.LineChartMostConfirmed, subRegionsListStats.toDomain())) }
        }.distinctUntilChanged()

    fun getCountriesAndStatsWithMostDeaths(): Flow<Either<DomainError, ListCountryAndStats>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostDeaths()) { countriesListOneStats ->
            countriesListOneStats.toPojoCountriesOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.toDomain())
            }
        }.distinctUntilChanged()

    fun getRegionsAndStatsWithMostDeaths(
        idCountry: String
    ): Flow<Either<DomainError, Pair<MenuItemViewType, ListRegionAndStats>>> =
        mapEntityValid(regionStatsDao.getRegionsAndStatsWithMostDeaths(idCountry)) {
            regionsListOneStats -> regionsListOneStats.toPojoRegionsOrdered().let {
                regionsListStats -> Pair(regionsListStats.isNotEmpty(),
                    Pair(MenuItemViewType.LineChartMostDeaths, regionsListStats.toDomain()))
            }
        }.distinctUntilChanged()

    fun getSubRegionsAndStatsWithMostDeaths(
        idCountry: String,
        idRegion: String
    ): Flow<Either<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>>> =
        mapEntityValid(subRegionStatsDao.getSubRegionsAndStatsWithMostDeaths(idCountry, idRegion)) {
            subRegionsListOneStats -> subRegionsListOneStats.toPojoSubRegionsOrdered().let {
                subRegionsListStats -> Pair(subRegionsListStats.isNotEmpty(),
                    Pair(MenuItemViewType.LineChartMostDeaths, subRegionsListStats.toDomain()))
            }
        }.distinctUntilChanged()

    fun getCountriesAndStatsWithMostRecovered(): Flow<Either<DomainError, ListCountryAndStats>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostRecovered()) { countriesListOneStats ->
            countriesListOneStats.toPojoCountriesOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.toDomain())
            }
        }.distinctUntilChanged()

    fun getRegionsAndStatsWithMostRecovered(
        idCountry: String
    ): Flow<Either<DomainError, Pair<MenuItemViewType, ListRegionAndStats>>> =
        mapEntityValid(regionStatsDao.getRegionsAndStatsWithMostRecovered(idCountry)) {
            regionsListOneStats -> regionsListOneStats.toPojoRegionsOrdered().let { regionsListStats ->
                Pair(regionsListStats.isNotEmpty(), Pair(MenuItemViewType.LineChartMostRecovered,
                    regionsListStats.toDomain()))
            }
        }.distinctUntilChanged()

    fun getSubRegionsAndStatsWithMostRecovered(
        idCountry: String,
        idRegion: String
    ): Flow<Either<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>>> =
        mapEntityValid(subRegionStatsDao.getSubRegionsAndStatsWithMostRecovered(idCountry, idRegion)) {
            subRegionsListOneStats -> subRegionsListOneStats.toPojoSubRegionsOrdered().let {
                subRegionsListStats -> Pair(subRegionsListStats.isNotEmpty(),
                    Pair(MenuItemViewType.LineChartMostRecovered, subRegionsListStats.toDomain()))
            }
        }.distinctUntilChanged()

    fun getCountriesAndStatsWithMostOpenCases(): Flow<Either<DomainError, ListCountryAndStats>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostOpenCases()) { countriesListOneStats ->
            countriesListOneStats.toPojoCountriesOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.toDomain())
            }
        }.distinctUntilChanged()

    fun getRegionsAndStatsWithMostOpenCases(
        idCountry: String
    ): Flow<Either<DomainError, Pair<MenuItemViewType, ListRegionAndStats>>> =
        mapEntityValid(regionStatsDao.getRegionsAndStatsWithMostOpenCases(idCountry)) { regionsListOneStats ->
            regionsListOneStats.toPojoRegionsOrdered().let { regionsListStats -> Pair(regionsListStats.isNotEmpty(),
                Pair(MenuItemViewType.LineChartMostOpenCases, regionsListStats.toDomain()))
            }
        }.distinctUntilChanged()

    fun getSubRegionsAndStatsWithMostOpenCases(
        idCountry: String,
        idRegion: String
    ): Flow<Either<DomainError, Pair<MenuItemViewType, ListSubRegionAndStats>>> =
        mapEntityValid(subRegionStatsDao.getSubRegionsAndStatsWithMostOpenCases(idCountry, idRegion)) {
            subRegionsListOneStats -> subRegionsListOneStats.toPojoSubRegionsOrdered().let {
                subRegionsListStats -> Pair(subRegionsListStats.isNotEmpty(),
                    Pair(MenuItemViewType.LineChartMostOpenCases, subRegionsListStats.toDomain()))
            }
        }.distinctUntilChanged()

    fun getCountries(): Flow<Either<DomainError, ListCountry>> =
        mapEntityValid(countryDao.getAll()) { countries ->
            Pair(countries.isNotEmpty(), countries.toDomain())
        }.distinctUntilChanged()

    fun getRegionsByCountry(idCountry: String): Flow<Either<DomainError, ListRegion>> =
        mapEntity(regionDao.getByCountry(idCountry)) { regions ->
            regions.toDomain()
        }.distinctUntilChanged()

    fun getCountryAndStatsByDate(
        idCountry: String,
        date: String
    ): Flow<Either<DomainError, CountryOneStats>> =
        when (date.isEmpty()) {
            true ->
                mapEntityValid(countryStatsDao.getCountryAndStatsByLastDate(idCountry)) {
                    country -> Pair(country.isValid(), country.toDomain())
                }
            false ->
                mapEntityValid(countryStatsDao.getCountryAndStatsByDate(idCountry,
                    date.dateToMilliseconds())) { country -> Pair(country.isValid(), country.toDomain())
                }
        }.distinctUntilChanged()

    fun getRegionAndStatsByDate(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<DomainError, RegionOneStats>> =
        when (date.isEmpty()) {
            true ->
                mapEntityValid(regionStatsDao.getRegionAndStatsByLastDate(idCountry, idRegion)) {
                    region -> Pair(region.isValid(), region.toDomain())
                }
            false ->
                mapEntityValid(regionStatsDao.getRegionAndStatsByDate(idCountry, idRegion,
                    date.dateToMilliseconds())) { region -> Pair(region.isValid(), region.toDomain())
                }
        }.distinctUntilChanged()

    suspend fun save(covidTracker: CovidTracker) = populateDatabase(listOf(covidTracker))

    suspend fun populateDatabase(covidTrackers: List<CovidTracker>) {
        // To avoid memory leaks. If you are using an emulator you can add more days.
        val maxDaysToSave = 1
        val worldStats = mutableListOf<WorldStatsEntity>()
        val countryEntities = mutableListOf<CountryEntity>()
        val countryStatsEntities = mutableListOf<CountryStatsEntity>()
        val regionEntities = mutableListOf<RegionEntity>()
        val regionStatsEntities = mutableListOf<RegionStatsEntity>()
        val subRegionEntities = mutableListOf<SubRegionEntity>()
        val subRegionStatsEntities = mutableListOf<SubRegionStatsEntity>()

        covidTrackers.mapIndexed { index, covidTracker ->
            worldStats.add(covidTracker.worldStats.toEntity())
            covidTracker.countriesStats.map { countryStats ->
                if (index == 0) {
                    countryEntities.add(countryStats.toEntity())
                    countryStats.regionStats?.map { regionStats ->
                        regionEntities.add(regionStats.region.toEntity(countryStats.country.id))
                        regionStats.subRegionStats?.map { subRegionStats ->
                            subRegionEntities.add(subRegionStats.subRegion.toEntity(
                                regionStats.region.id, countryStats.country.id))
                        }
                    }
                }
                countryStatsEntities.add(countryStats.stats.toEntity(countryStats.country.id))
                countryStats.regionStats?.map { regionStats ->
                    regionStatsEntities.add(regionStats.toEntity(
                        regionStats.region.id, countryStats.country.id))

                    regionStats.subRegionStats?.map { subRegionStats ->
                        subRegionStatsEntities.add(
                            subRegionStats.toEntity(subRegionStats.subRegion.id, regionStats.region.id))
                    }
                }
            }

            if (index.rem(maxDaysToSave) == 0 || index == covidTrackers.size - 1) {
                covidTrackerDao.populateDatabase(
                    worldsStats = worldStats,
                    countries = countryEntities,
                    countriesStats = countryStatsEntities,
                    regions = regionEntities,
                    regionsStats = regionStatsEntities,
                    subRegions = subRegionEntities,
                    subRegionsStats = subRegionStatsEntities
                )

                worldStats.clear()
                countryEntities.clear()
                countryStatsEntities.clear()
                regionEntities.clear()
                regionStatsEntities.clear()
                subRegionEntities.clear()
                subRegionStatsEntities.clear()
            }
        }
    }
}