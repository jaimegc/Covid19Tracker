package com.jaimegc.covid19tracker.data.repository

import arrow.core.Either
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.extensions.mapResponse
import com.jaimegc.covid19tracker.data.api.model.toDomain
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.flow.Flow

class CovidTrackerRepository(
    private val apiClient: CovidTrackerApiClient
) {

    fun getCovidTrackerLast(): Flow<Either<StateError<DomainError>, State<CovidTracker>>> {
        return object : NetworkRepository<DomainError, CovidTracker>() {
            override suspend fun fetchFromRemoteEither(): Either<DomainError, CovidTracker> =
                mapResponse(apiClient.getCovidTrackerLast()) { covidTracker -> covidTracker.toDomain() }
        }.asFlowEither()
    }
}