package com.jaimegc.covid19tracker.data.datasource

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.extensions.apiException
import com.jaimegc.covid19tracker.data.api.extensions.mapResponse
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.StatsEntity
import com.jaimegc.covid19tracker.data.room.entities.WorldStatsEntity
import com.jaimegc.covid19tracker.domain.model.toDomain
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
    private val covidTrackerDao: CovidTrackerDao
) {

    suspend fun getCovidTrackerByDate(date: String): Flow<Either<DomainError, CovidTracker>> =
        mapEntityValid(covidTrackerDao.getWorldAndCountriesStatsByDateDV(date)) { covidTrackerPojo ->
            Pair(covidTrackerPojo.isValid(), covidTrackerPojo.toDomain()) }

    suspend fun save(covidTracker: CovidTracker) {
        val countryEntities = mutableListOf<CountryEntity>()
        val countryStatsEntities = mutableListOf<StatsEntity>()

        covidTracker.countriesStats.map { countryStats ->
            countryEntities.add(countryStats.toEntity())
            countryStatsEntities.add(countryStats.stats.toEntity(countryStats.id))
        }

        covidTrackerDao.save(covidTracker.worldStats.toEntity(), countryEntities, countryStatsEntities)
    }

    suspend fun populateDatabase(covidTrackers: List<CovidTracker>) {
        val maxDaysToSave = 7 // To avoid memory leaks
        val worldStatsEntities = mutableListOf<WorldStatsEntity>()
        val countryEntities = mutableListOf<CountryEntity>()
        val countryStatsEntities = mutableListOf<StatsEntity>()

        covidTrackers.mapIndexed { index, covidTracker ->
            worldStatsEntities.add(covidTracker.worldStats.toEntity())
            covidTracker.countriesStats.map { countryStats ->
                if (index == 0) countryEntities.add(countryStats.toEntity())
                countryStatsEntities.add(countryStats.stats.toEntity(countryStats.id))
            }

            if (index.rem(maxDaysToSave) == 0 || index == covidTrackers.size - 1) {
                covidTrackerDao.populateDatabase(worldStatsEntities, countryEntities, countryStatsEntities)
                worldStatsEntities.clear()
                countryEntities.clear()
                countryStatsEntities.clear()
            }
        }
    }
}