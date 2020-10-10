package com.jaimegc.covid19tracker.domain.usecase

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.ListSubRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionStats
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import kotlinx.coroutines.flow.Flow

class GetSubRegionStats(
    private val repository: CovidTrackerRepository
) {

    fun getSubRegionsStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String,
        date: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionStats>>> =
        repository.getSubRegionsStatsOrderByConfirmed(idCountry, idRegion, date)

    fun getSubRegionsAndStatsWithMostConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListSubRegionAndStats>>>> =
        repository.getSubRegionsAndStatsWithMostConfirmed(idCountry, idRegion)

    fun getSubRegionsAndStatsWithMostDeaths(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListSubRegionAndStats>>>> =
        repository.getSubRegionsAndStatsWithMostDeaths(idCountry, idRegion)

    fun getSubRegionsAndStatsWithMostRecovered(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListSubRegionAndStats>>>> =
        repository.getSubRegionsAndStatsWithMostRecovered(idCountry, idRegion)

    fun getSubRegionsAndStatsWithMostOpenCases(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<Pair<MenuItemViewType, ListSubRegionAndStats>>>> =
        repository.getSubRegionsAndStatsWithMostOpenCases(idCountry, idRegion)

    fun getSubRegionsAllStatsOrderByConfirmed(
        idCountry: String,
        idRegion: String
    ): Flow<Either<StateError<DomainError>, State<ListSubRegionAndStats>>> =
        repository.getSubRegionsAllStatsOrderByConfirmed(idCountry, idRegion)
}