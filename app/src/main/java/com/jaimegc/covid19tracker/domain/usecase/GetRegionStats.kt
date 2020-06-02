package com.jaimegc.covid19tracker.domain.usecase

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import kotlinx.coroutines.flow.Flow

class GetRegionStats(
    private val repository: CovidTrackerRepository
) {

    fun getRegionsStatsOrderByConfirmed(
        idCountry: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionStats>>> =
        repository.getRegionsStatsOrderByConfirmed(idCountry, date)

    fun getRegionsAllStatsOrderByConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionAndStats>>> =
        repository.getRegionsAllStatsOrderByConfirmed(idCountry)

    fun getRegionsAndStatsWithMostConfirmed(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListRegionAndStats>>>> =
        repository.getRegionsAndStatsWithMostConfirmed(idCountry)

    fun getRegionsAndStatsWithMostDeaths(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListRegionAndStats>>>> =
        repository.getRegionsAndStatsWithMostDeaths(idCountry)

    fun getRegionsAndStatsWithMostRecovered(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListRegionAndStats>>>> =
        repository.getRegionsAndStatsWithMostRecovered(idCountry)

    fun getRegionsAndStatsWithMostOpenCases(
        idCountry: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListRegionAndStats>>>> =
        repository.getRegionsAndStatsWithMostOpenCases(idCountry)

    fun getRegionAndStatsByDate(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<RegionOneStats>>> =
        repository.getRegionAndStatsByDate(idCountry, idRegion, date)

    fun getRegionAllStats(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListRegionOnlyStats>>> =
        repository.getRegionAllStats(idCountry, idRegion)
}