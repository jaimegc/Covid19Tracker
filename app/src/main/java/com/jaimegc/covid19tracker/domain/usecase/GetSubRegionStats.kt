package com.jaimegc.covid19tracker.domain.usecase

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.flow.Flow

class GetSubRegionStats(
    private val repository: CovidTrackerRepository
) {

    suspend fun getSubRegionsStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionStats>>> =
        repository.getSubRegionsStatsOrderByConfirmed(idCountry, idRegion, date)

    suspend fun getSubRegionsAndStatsWithMostConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        repository.getSubRegionsAndStatsWithMostConfirmed(idCountry, idRegion)

    suspend fun getSubRegionsAndStatsWithMostDeaths(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        repository.getSubRegionsAndStatsWithMostDeaths(idCountry, idRegion)

    suspend fun getSubRegionsAndStatsWithMostRecovered(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        repository.getSubRegionsAndStatsWithMostRecovered(idCountry, idRegion)

    suspend fun getSubRegionsAndStatsWithMostOpenCases(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        repository.getSubRegionsAndStatsWithMostOpenCases(idCountry, idRegion)

    suspend fun getSubRegionsAllStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        repository.getSubRegionsAllStatsOrderByConfirmed(idCountry, idRegion)
}