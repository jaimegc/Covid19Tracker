package com.jaimegc.covid19tracker.ui.country

import androidx.lifecycle.*
import arrow.core.Either
import com.jaimegc.covid19tracker.common.QueueLiveData
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.*
import com.jaimegc.covid19tracker.ui.model.*
import com.jaimegc.covid19tracker.ui.base.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import com.jaimegc.covid19tracker.ui.base.BaseScreenStateMenuViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class CountryViewModel(
    private val getCountry: GetCountry,
    private val getCountryStats: GetCountryStats,
    private val getRegion: GetRegion,
    private val getRegionStats: GetRegionStats,
    private val getSubRegionStats: GetSubRegionStats
) : BaseScreenStateMenuViewModel<PlaceStateScreen>() {

    override val _screenState = QueueLiveData<ScreenState<PlaceStateScreen>>()
    override val screenState: LiveData<ScreenState<PlaceStateScreen>> = _screenState

    private val mapPlacesLineStats =
        mutableMapOf<MenuItemViewType, List<PlaceListStatsChartUI>>()

    private var jobListOrPieChart: Job? = null
    private var jobBarChart: Job? = null
    private var jobLineChart: Job? = null

    fun getCountries() {
        cancelAllCharts()
        viewModelScope.launch {
            getCountry.getCountries().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it) }
                )
            }
        }
    }

    fun getRegionsByCountry(idCountry: String) {
        cancelAllCharts()
        viewModelScope.launch {
            getRegion.getRegionsByCountry(idCountry).collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it) }
                )
            }
        }
    }

    fun getListStats(idCountry: String, idRegion: String = "") =
        listOrPieChartStats(idCountry, idRegion, MenuItemViewType.List)

    fun getPieChartStats(idCountry: String, idRegion: String = "") =
        listOrPieChartStats(idCountry, idRegion, MenuItemViewType.PieChart)

    private fun listOrPieChartStats(idCountry: String, idRegion: String, viewType: MenuItemViewType) {
        cancelAllCharts()
        jobListOrPieChart = viewModelScope.launch {
            val date = "2020-05-24"

            if (idRegion.isEmpty()) {
                val countryAndStatsByDate =
                    getCountryStats.getCountryAndStatsByDate(idCountry, date)
                val regionsStatsOrderByConfirmed =
                    getRegionStats.getRegionsStatsOrderByConfirmed(idCountry, date)

                // Zip to wait for all values
                countryAndStatsByDate.zip(regionsStatsOrderByConfirmed) { countries, regions ->
                    listOf(countries, regions)
                }.collect { results ->
                    results.map { result ->
                        result.fold(
                            { handleError(it) },
                            { handleState(state = it, viewType = viewType) }
                        )
                    }
                }
            } else {
                val regionAndStatsByDate =
                    getRegionStats.getRegionAndStatsByDate(idCountry, idRegion, date)
                val subRegionsStatsOrderByConfirmed =
                    getSubRegionStats.getSubRegionsStatsOrderByConfirmed(idCountry, idRegion, date)

                // Zip to wait for all values
                regionAndStatsByDate.zip(subRegionsStatsOrderByConfirmed) { regions, subRegions ->
                    listOf(regions, subRegions)
                }.collect { results ->
                    results.map { result ->
                        result.fold(
                            { handleError(it) },
                            { handleState(state = it, viewType = viewType) }
                        )
                    }
                }
            }
        }
    }

    fun getBarChartStats(idCountry: String, idRegion: String = "") {
        cancelAllCharts()
        jobBarChart = viewModelScope.launch {
            if (idRegion.isEmpty()) {
                val countryStats = getCountryStats.getCountryAllStats(idCountry)
                val regionStats = getRegionStats.getRegionsAllStatsOrderByConfirmed(idCountry)

                // Combine to not wait for all values. Requests are a bit heavy
                countryStats.combine(regionStats) { countries, regions ->
                    listOf(countries, regions)
                }.collect { results ->
                    results.map { result ->
                        result.fold(
                            { handleError(it) },
                            { handleState(state = it, viewType = MenuItemViewType.BarChart) }
                        )
                    }
                }
            } else {
                val regionStats = getRegionStats.getRegionAllStats(idCountry, idRegion)
                val subRegionStats = getSubRegionStats.getSubRegionsAllStatsOrderByConfirmed(idCountry, idRegion)

                // Combine to not wait for all values. Requests are a bit heavy
                regionStats.combine(subRegionStats) { regions, subRegions ->
                    listOf(regions, subRegions)
                }.collect { results ->
                    results.map { result ->
                        result.fold(
                            { handleError(it) },
                            { handleState(state = it, viewType = MenuItemViewType.BarChart) }
                        )
                    }
                }
            }
        }
    }

    fun getLineChartStats(idCountry: String, idRegion: String = "") {
        cancelAllCharts()
        jobLineChart = viewModelScope.launch {
            mapPlacesLineStats.clear()

            val allRequests = mutableListOf<Flow<Either<StateError<DomainError>, State<*>>>>()

            if (idRegion.isEmpty()) {
                allRequests.add(getRegionStats.getRegionsAndStatsWithMostConfirmed(idCountry))
                allRequests.add(getRegionStats.getRegionsAndStatsWithMostDeaths(idCountry))
                allRequests.add(getRegionStats.getRegionsAndStatsWithMostRecovered(idCountry))
                allRequests.add(getRegionStats.getRegionsAndStatsWithMostOpenCases(idCountry))
            } else {
                allRequests.add(
                    getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(
                        idCountry,
                        idRegion
                    )
                )
                allRequests.add(
                    getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(
                        idCountry,
                        idRegion
                    )
                )
                allRequests.add(
                    getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(
                        idCountry,
                        idRegion
                    )
                )
                allRequests.add(
                    getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(
                        idCountry,
                        idRegion
                    )
                )
            }

            // flatMapMerge to not wait for all values. Requests are a bit heavy
            (allRequests.indices).asFlow()
                .flatMapMerge { allRequests[it] }
                .collect { result ->
                    result.fold(
                        { handleError(it) },
                        { handleState(state = it) }
                    )
                }
        }
    }

    override suspend fun <T> handleState(
        state: State<T>,
        viewType: MenuItemViewType
    ) {
        when (state) {
            is State.Success -> {
                when (state.data) {
                    is ListCountry ->
                        _screenState.postValue(ScreenState.Render(PlaceStateScreen.SuccessSpinnerCountries(
                            state.data.countries.map { country -> country.toUI() })))
                    is ListRegion ->
                        _screenState.postValue(ScreenState.Render(PlaceStateScreen.SuccessSpinnerRegions(
                            state.data.regions.map { region -> region.toPlaceUI() })))
                    is CountryOneStats ->
                        when (viewType) {
                            is MenuItemViewType.List ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessPlaceAndStats(state.data.toPlaceUI())))
                            is MenuItemViewType.PieChart ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessCountryAndStatsPieChart(state.data.stats.toChartUI())))
                        }
                    is RegionOneStats ->
                        when (viewType) {
                            is MenuItemViewType.List ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessPlaceAndStats(state.data.toPlaceUI())))
                            is MenuItemViewType.PieChart ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessCountryAndStatsPieChart(state.data.stats.toChartUI())))
                        }
                    is ListRegionStats ->
                        when (viewType) {
                            is MenuItemViewType.List ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessPlaceStats(state.data.toPlaceUI())))
                            is MenuItemViewType.PieChart ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessRegionAndStatsPieChart(state.data.toPlaceChartUI())))
                        }
                    is ListSubRegionStats ->
                        when (viewType) {
                            is MenuItemViewType.List ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessPlaceStats(state.data.toPlaceUI())))
                            is MenuItemViewType.PieChart ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessRegionAndStatsPieChart(state.data.toPlaceChartUI())))
                        }
                    is ListCountryOnlyStats ->
                        _screenState.postValue(ScreenState.Render(
                            PlaceStateScreen.SuccessPlaceTotalStatsBarChart(state.data.toPlaceUI())))
                    is ListRegionOnlyStats ->
                        _screenState.postValue(ScreenState.Render(
                            PlaceStateScreen.SuccessPlaceTotalStatsBarChart(state.data.toPlaceUI())))
                    is ListRegionAndStats ->
                        when (viewType) {
                            is MenuItemViewType.BarChart ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessPlaceStatsBarChart(state.data.toPlaceUI())))
                        }
                    is ListSubRegionAndStats ->
                        when (viewType) {
                            is MenuItemViewType.BarChart ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessPlaceStatsBarChart(state.data.toPlaceUI())))
                        }
                    is Pair<*, *> -> {
                        val menuViewType = state.data.first as MenuItemViewType

                        when (state.data.second) {
                            is ListRegionAndStats ->
                                mapPlacesLineStats[menuViewType] =
                                    (state.data.second as ListRegionAndStats).toPlaceUI()
                            is ListSubRegionAndStats ->
                                mapPlacesLineStats[menuViewType] =
                                    (state.data.second as ListSubRegionAndStats).toPlaceUI()
                        }

                        _screenState.postValue(ScreenState.Render(
                            PlaceStateScreen.SuccessPlaceStatsLineCharts(mapPlacesLineStats)))
                    }
                }
            }
            is State.Loading ->
                _screenState.postValue(ScreenState.Loading)
            is State.EmptyData ->
                _screenState.postValue(ScreenState.EmptyData)
        }
    }

    private fun handleError(state: StateError<DomainError>) {
        // Not implemented
    }

    private fun cancelAllCharts() {
        jobListOrPieChart?.cancel()
        jobBarChart?.cancel()
        jobLineChart?.cancel()
    }
}