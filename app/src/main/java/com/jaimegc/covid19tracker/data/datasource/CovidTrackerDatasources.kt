package com.jaimegc.covid19tracker.data.datasource

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.extensions.apiException
import com.jaimegc.covid19tracker.data.api.extensions.mapResponse
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerDao
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.ui.model.toEntity
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

    fun getCovidTrackerLast(): Flow<Either<DomainError, CovidTracker>> =
        mapEntityValid(covidTrackerDao.getWorldAndCountriesByDate("2020-04-15")) { covidTrackerPojo ->
            Pair(covidTrackerPojo.isValid(), covidTrackerPojo.toDomain()) }

    suspend fun save(covidTracker: CovidTracker) =
        covidTrackerDao.save(covidTracker.worldStats.toEntity(),
            covidTracker.countriesStats.map { countryStats ->
                countryStats.toEntity(covidTracker.worldStats.date)
            })

}