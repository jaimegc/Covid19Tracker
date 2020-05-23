package com.jaimegc.covid19tracker.data.datasource

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.extensions.apiException
import com.jaimegc.covid19tracker.data.api.extensions.mapResponse
import com.jaimegc.covid19tracker.data.room.daos.*
import com.jaimegc.covid19tracker.data.room.entities.*
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.domain.model.toPojoRegionDomain
import com.jaimegc.covid19tracker.domain.model.toStatsDomain
import com.jaimegc.covid19tracker.domain.model.toPojoCountriesOrdered
import com.jaimegc.covid19tracker.domain.model.*
import kotlinx.coroutines.flow.Flow

class RemoteCovidTrackerDatasource(
    private val apiClient: CovidTrackerApiClient
) {
    suspend fun getCovidTrackerByDate(date: String): Either<DomainError, CovidTracker> =
        try {
            mapResponse(apiClient.getCovidTrackerByDate(date)) { covidTracker -> covidTracker.toDomain(date) }
        } catch (exception: Exception) {
            Either.left(exception.apiException())
        }
}

class LocalCovidTrackerDatasource(
    private val covidTrackerDao: CovidTrackerDao,
    private val worldStatsDao: WorldStatsDao,
    private val countryStatsDao: CountryStatsDao,
    private val countryDao: CountryDao,
    private val regionDao: RegionDao,
    private val regionStatsDao: RegionStatsDao,
    private val subRegionStatsDao: SubRegionStatsDao
) {

    suspend fun getCovidTrackerByDate(date: String): Flow<Either<DomainError, CovidTracker>> =
        mapEntityValid(covidTrackerDao.getWorldAndCountriesStatsByDate(date)) { covidTrackerPojo ->
            Pair(covidTrackerPojo.isValid(), covidTrackerPojo.toDomain()) }

    suspend fun getWorldAllStats(): Flow<Either<DomainError, ListWorldStats>> =
        mapEntityValid(worldStatsDao.getAll()) { worldEntities ->
            Pair(worldEntities.isNotEmpty(), worldEntities.toDomain()) }

    suspend fun getCountryAllStats(idCountry: String): Flow<Either<DomainError, ListCountryOnlyStats>> =
        mapEntityValid(countryStatsDao.getById(idCountry)) { countryEntities ->
            Pair(countryEntities.isNotEmpty(), countryEntities.toStatsDomain()) }

    suspend fun getRegionAllStats(
        idCountry: String,
        idRegion: String
    ): Flow<Either<DomainError, ListRegionOnlyStats>> =
        mapEntityValid(regionStatsDao.getById(idCountry, idRegion)) { regionEntities ->
            Pair(regionEntities.isNotEmpty(), regionEntities.toStatsDomain()) }

    suspend fun getCountriesStatsOrderByConfirmed(): Flow<Either<DomainError, ListCountryAndStats>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsOrderByConfirmed()) { countriesListStats ->
            Pair(countriesListStats.isNotEmpty(), countriesListStats.toDomain()) }

    suspend fun getRegionsStatsOrderByConfirmed(
        idCountry: String,
        date: String
    ): Flow<Either<DomainError, ListRegionStats>> =
        mapEntityValid(regionStatsDao.getRegionAndStatsByCountryAndDateOrderByConfirmed(idCountry, date)) {
            regionsListStats -> Pair(regionsListStats.isNotEmpty(), regionsListStats.toDomain(date)) }

    suspend fun getSubRegionsStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<DomainError, ListSubRegionStats>> =
        mapEntityValid(subRegionStatsDao.getSubRegionAndStatsByCountryAndDateOrderByConfirmed(idCountry, idRegion, date)) {
                regionsListStats -> Pair(regionsListStats.isNotEmpty(), regionsListStats.toDomain(date)) }

    suspend fun getRegionsAllStatsOrderByConfirmed(
        idCountry: String
    ): Flow<Either<DomainError, ListRegionAndStats>> =
        mapEntityValid(regionStatsDao.getRegionAndAllStatsByCountryAndDateOrderByConfirmed(idCountry)) {
            regionsListStats -> Pair(regionsListStats.isNotEmpty(), regionsListStats.toPojoRegionDomain()) }

    suspend fun getSubRegionsAllStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<DomainError, ListSubRegionAndStats>> =
        mapEntityValid(subRegionStatsDao.getSubRegionAndAllStatsByCountryAndDateOrderByConfirmed(idCountry, idRegion)) {
                regionsListStats -> Pair(regionsListStats.isNotEmpty(), regionsListStats.toPojoSubRegionDomain()) }

    suspend fun getCountriesAndStatsWithMostConfirmed(): Flow<Either<DomainError, ListCountryAndStats>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostConfirmed()) { countriesListOneStats ->
            countriesListOneStats.toPojoCountriesOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.toDomain()) }
            }

    suspend fun getRegionsAndStatsWithMostConfirmed(idCountry: String): Flow<Either<DomainError, ListRegionAndStats>> =
        mapEntityValid(regionStatsDao.getRegionsAndStatsWithMostConfirmed(idCountry)) { regionsListOneStats ->
            regionsListOneStats.toPojoRegionsOrdered().let { regionsListStats ->
                Pair(regionsListStats.isNotEmpty(), regionsListStats.toDomain()) }
        }

    suspend fun getCountriesAndStatsWithMostDeaths(): Flow<Either<DomainError, ListCountryAndStats>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostDeaths()) { countriesListOneStats ->
            countriesListOneStats.toPojoCountriesOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.toDomain()) }
        }

    suspend fun getRegionsAndStatsWithMostDeaths(idCountry: String): Flow<Either<DomainError, ListRegionAndStats>> =
        mapEntityValid(regionStatsDao.getRegionsAndStatsWithMostDeaths(idCountry)) { regionsListOneStats ->
            regionsListOneStats.toPojoRegionsOrdered().let { regionsListStats ->
                Pair(regionsListStats.isNotEmpty(), regionsListStats.toDomain()) }
        }

    suspend fun getCountriesAndStatsWithMostRecovered(): Flow<Either<DomainError, ListCountryAndStats>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostRecovered()) { countriesListOneStats ->
            countriesListOneStats.toPojoCountriesOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.toDomain()) }
        }

    suspend fun getRegionsAndStatsWithMostRecovered(idCountry: String): Flow<Either<DomainError, ListRegionAndStats>> =
        mapEntityValid(regionStatsDao.getRegionsAndStatsWithMostRecovered(idCountry)) { regionsListOneStats ->
            regionsListOneStats.toPojoRegionsOrdered().let { regionsListStats ->
                Pair(regionsListStats.isNotEmpty(), regionsListStats.toDomain()) }
        }

    suspend fun getCountriesAndStatsWithMostOpenCases(): Flow<Either<DomainError, ListCountryAndStats>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostOpenCases()) { countriesListOneStats ->
            countriesListOneStats.toPojoCountriesOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.toDomain()) }
        }

    suspend fun getRegionsAndStatsWithMostOpenCases(idCountry: String): Flow<Either<DomainError, ListRegionAndStats>> =
        mapEntityValid(regionStatsDao.getRegionsAndStatsWithMostOpenCases(idCountry)) { regionsListOneStats ->
            regionsListOneStats.toPojoRegionsOrdered().let { regionsListStats ->
                Pair(regionsListStats.isNotEmpty(), regionsListStats.toDomain()) }
        }

    suspend fun getCountries(): Flow<Either<DomainError, ListCountry>> =
        mapEntityValid(countryDao.getAll()) { countries ->
            Pair(countries.isNotEmpty(), countries.toDomain())
        }

    suspend fun getRegionsByCountry(idCountry: String): Flow<Either<DomainError, ListRegion>> =
        mapEntity(regionDao.getByCountry(idCountry)) { regions -> regions.toDomain() }

    suspend fun getCountryAndStatsByDate(
        idCountry: String,
        date: String
    ): Flow<Either<DomainError, CountryOneStats>> =
        mapEntityValid(countryStatsDao.getCountryAndStatsByDate(idCountry, date)) { country ->
            Pair(country.isValid(), country.toDomain())
        }

    suspend fun getRegionAndStatsByDate(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<DomainError, RegionOneStats>> =
        mapEntityValid(regionStatsDao.getRegionAndStatsByDate(idCountry, idRegion, date)) { region ->
            Pair(region.isValid(), region.toDomain())
        }

    suspend fun save(covidTracker: CovidTracker) = populateDatabase(listOf(covidTracker))

    suspend fun populateDatabase(covidTrackers: List<CovidTracker>) {
        val maxDaysToSave = 7 // To avoid memory leaks
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