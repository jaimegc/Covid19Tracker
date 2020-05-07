package com.jaimegc.covid19tracker.data.datasource

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.extensions.apiException
import com.jaimegc.covid19tracker.data.api.extensions.mapResponse
import com.jaimegc.covid19tracker.data.room.daos.CountryStatsDao
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.data.room.daos.WorldStatsDao
import com.jaimegc.covid19tracker.data.room.entities.*
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.domain.model.toPojoOrdered
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
    private val countryStatsDao: CountryStatsDao
) {

    suspend fun getCovidTrackerByDate(date: String): Flow<Either<DomainError, CovidTracker>> =
        mapEntityValid(covidTrackerDao.getWorldAndCountriesStatsByDate(date)) { covidTrackerPojo ->
            Pair(covidTrackerPojo.isValid(), covidTrackerPojo.toDomain()) }

    suspend fun getWorldAllStats(): Flow<Either<DomainError, List<WorldStats>>> =
        mapEntityValid(worldStatsDao.getAll()) { worldEntities ->
            Pair(worldEntities.isNotEmpty(), worldEntities.map { worldEntity -> worldEntity.toDomain() }) }

    suspend fun getCountriesStatsOrderByConfirmed(): Flow<Either<DomainError, List<CountryListStats>>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsOrderByConfirmed()) { countriesListStats ->
            Pair(countriesListStats.isNotEmpty(), countriesListStats.map { countryStats -> countryStats.toDomain() }) }

    suspend fun getCountriesAndStatsWithMostConfirmed(): Flow<Either<DomainError, List<CountryListStats>>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostConfirmed()) { countriesListOneStats ->
            countriesListOneStats.toPojoOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.map { countryStats ->
                    countryStats.toDomain() }) }
            }

    suspend fun getCountriesAndStatsWithMostDeaths(): Flow<Either<DomainError, List<CountryListStats>>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostDeaths()) { countriesListOneStats ->
            countriesListOneStats.toPojoOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.map { countryStats ->
                    countryStats.toDomain() }) }
        }

    suspend fun getCountriesAndStatsWithMostRecovered(): Flow<Either<DomainError, List<CountryListStats>>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostRecovered()) { countriesListOneStats ->
            countriesListOneStats.toPojoOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.map { countryStats ->
                    countryStats.toDomain() }) }
        }

    suspend fun getCountriesAndStatsWithMostOpenCases(): Flow<Either<DomainError, List<CountryListStats>>> =
        mapEntityValid(countryStatsDao.getCountriesAndStatsWithMostOpenCases()) { countriesListOneStats ->
            countriesListOneStats.toPojoOrdered().let { countriesListStats ->
                Pair(countriesListStats.isNotEmpty(), countriesListStats.map { countryStats ->
                    countryStats.toDomain() }) }
        }

    suspend fun getCountries(): Flow<Either<DomainError, List<Country>>> =
        mapEntityValid(countryStatsDao.getAll()) { countries ->
            Pair(countries.isNotEmpty(), countries.map { country -> country.toDomain() }) }

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