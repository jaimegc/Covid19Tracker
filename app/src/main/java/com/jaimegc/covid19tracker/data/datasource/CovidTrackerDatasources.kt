package com.jaimegc.covid19tracker.data.datasource

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.extensions.mapResponse
import com.jaimegc.covid19tracker.data.api.model.toDomain
import com.jaimegc.covid19tracker.data.room.daos.CovidTrackerTotalDao
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError

class RemoteCovidTrackerDatasource(
    private val apiClient: CovidTrackerApiClient
) {
    suspend fun getCovidTrackerLast(): Either<DomainError, CovidTracker> =
        mapResponse(apiClient.getCovidTrackerLast()) { covidTracker -> covidTracker.toDomain() }
}

class LocalCovidTrackerDatasource(
    private val covidTrackerTotalDao: CovidTrackerTotalDao
) {

}