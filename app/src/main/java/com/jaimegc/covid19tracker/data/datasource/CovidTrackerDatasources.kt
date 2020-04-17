package com.jaimegc.covid19tracker.data.datasource

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.extensions.apiException
import com.jaimegc.covid19tracker.data.api.extensions.mapResponse
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.data.room.entities.CountryEntity
import com.jaimegc.covid19tracker.data.room.entities.StatsEntity
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.domain.model.*
import kotlinx.coroutines.flow.Flow

class RemoteCovidTrackerDatasource(
    private val apiClient: CovidTrackerApiClient
) {
    suspend fun getCovidTrackerLast(): Either<DomainError, CovidTracker> =
        try {
            mapResponse(apiClient.getCovidTrackerLast()) { covidTracker -> covidTracker.toDomain() }
        } catch (exception: Exception) {
            Either.left(exception.apiException())
        }
}

class LocalCovidTrackerDatasource(
    private val covidTrackerDao: CovidTrackerDao
) {

    suspend fun getCovidTrackerLast(): Flow<Either<DomainError, CovidTracker>> =
        mapEntityValid(covidTrackerDao.getWorldAndCountriesStatsByDateDV("2020-04-16")) { covidTrackerPojo ->
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
}