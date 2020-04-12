package com.jaimegc.covid19tracker.data.repository

import arrow.core.Either
import com.jaimegc.covid19tracker.data.datasource.LocalCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.flow.Flow

class CovidTrackerRepository(
    private val localDatasource: LocalCovidTrackerDatasource,
    private val remoteDatasource: RemoteCovidTrackerDatasource
) {

    fun getCovidTrackerLast(): Flow<Either<StateError<DomainError>, State<CovidTracker>>> {
        return object : BaseRepository<DomainError, CovidTracker> {
            override suspend fun fetchFromLocal(): Either<DomainError, CovidTracker> =
                remoteDatasource.getCovidTrackerLast()

            override suspend fun fetchFromRemote(): Either<DomainError, CovidTracker> =
                remoteDatasource.getCovidTrackerLast()
        }.asFlow(CachePolicy.NetworkOnly)
    }
}