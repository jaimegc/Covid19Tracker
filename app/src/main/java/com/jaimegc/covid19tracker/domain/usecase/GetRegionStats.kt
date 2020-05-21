package com.jaimegc.covid19tracker.domain.usecase

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.flow.Flow

class GetRegionStats(
    private val repository: CovidTrackerRepository
) {

    suspend fun getRegionsStatsOrderByConfirmed(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionStats>>> =
        repository.getRegionsStatsOrderByConfirmed(idCountry, date)

    suspend fun getRegionsAllStatsOrderByConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        repository.getRegionsAllStatsOrderByConfirmed(idCountry)

    suspend fun getRegionsAndStatsWithMostConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        repository.getRegionsAndStatsWithMostConfirmed(idCountry)

    suspend fun getRegionsAndStatsWithMostDeaths(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        repository.getRegionsAndStatsWithMostDeaths(idCountry)

    suspend fun getRegionsAndStatsWithMostRecovered(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        repository.getRegionsAndStatsWithMostRecovered(idCountry)

    suspend fun getRegionsAndStatsWithMostOpenCases(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        repository.getRegionsAndStatsWithMostOpenCases(idCountry)

    suspend fun getRegionAndStatsByDate(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<RegionOneStats>>> =
        repository.getRegionAndStatsByDate(idCountry, idRegion, date)
}