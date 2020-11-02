package com.jaimegc.covid19tracker.ui.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.jaimegc.covid19tracker.common.QueueLiveData
import com.jaimegc.covid19tracker.domain.model.CountryOneStats
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.ListCountry
import com.jaimegc.covid19tracker.domain.model.ListCountryOnlyStats
import com.jaimegc.covid19tracker.domain.model.ListRegion
import com.jaimegc.covid19tracker.domain.model.ListRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListRegionOnlyStats
import com.jaimegc.covid19tracker.domain.model.ListRegionStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionAndStats
import com.jaimegc.covid19tracker.domain.model.ListSubRegionStats
import com.jaimegc.covid19tracker.domain.model.RegionOneStats
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetRegion
import com.jaimegc.covid19tracker.domain.usecase.GetRegionStats
import com.jaimegc.covid19tracker.domain.usecase.GetSubRegionStats
import com.jaimegc.covid19tracker.ui.base.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import com.jaimegc.covid19tracker.ui.base.BaseScreenStateMenuViewModel
import com.jaimegc.covid19tracker.ui.model.PlaceListStatsChartUI
import com.jaimegc.covid19tracker.ui.model.toChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceChartUI
import com.jaimegc.covid19tracker.ui.model.toPlaceUI
import com.jaimegc.covid19tracker.ui.model.toUI
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class CountryViewModel(
    private val getCountry: GetCountry,
    private val getCountryStats: GetCountryStats,
    private val getRegion: GetRegion,
    private val getRegionStats: GetRegionStats,
    private val getSubRegionStats: GetSubRegionStats
) : BaseScreenStateMenuViewModel<PlaceStateScreen>() {

    override val screenStateQueue = QueueLiveData<ScreenState<PlaceStateScreen>>()
    override val screenState: LiveData<ScreenState<PlaceStateScreen>> = screenStateQueue

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
            val date = "" // Empty to get the last date or use yyyy-MM-dd

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
                    getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(idCountry, idRegion)
                )
                allRequests.add(
                    getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(idCountry, idRegion)
                )
                allRequests.add(
                    getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(idCountry, idRegion)
                )
                allRequests.add(
                    getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(idCountry, idRegion)
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
                        screenStateQueue.postValue(
                            ScreenState.Render(
                                PlaceStateScreen.SuccessSpinnerCountries(
                                    state.data.countries.map { country -> country.toUI() }
                                )
                            )
                        )
                    is ListRegion ->
                        screenStateQueue.postValue(
                            ScreenState.Render(
                                PlaceStateScreen.SuccessSpinnerRegions(
                                    state.data.regions.map { region -> region.toPlaceUI() }
                                )
                            )
                        )
                    is CountryOneStats ->
                        when (viewType) {
                            is MenuItemViewType.List ->
                                screenStateQueue.postValue(
                                    ScreenState.Render(
                                        PlaceStateScreen.SuccessPlaceAndStats(state.data.toPlaceUI())
                                    )
                                )
                            is MenuItemViewType.PieChart ->
                                screenStateQueue.postValue(
                                    ScreenState.Render(
                                        PlaceStateScreen.SuccessPlaceTotalStatsPieChart(state.data.stats.toChartUI())
                                    )
                                )
                        }
                    is RegionOneStats ->
                        when (viewType) {
                            is MenuItemViewType.List ->
                                screenStateQueue.postValue(
                                    ScreenState.Render(
                                        PlaceStateScreen.SuccessPlaceAndStats(state.data.toPlaceUI())
                                    )
                                )
                            is MenuItemViewType.PieChart ->
                                screenStateQueue.postValue(
                                    ScreenState.Render(
                                        PlaceStateScreen.SuccessPlaceTotalStatsPieChart(state.data.stats.toChartUI())
                                    )
                                )
                        }
                    is ListRegionStats ->
                        when (viewType) {
                            is MenuItemViewType.List ->
                                screenStateQueue.postValue(
                                    ScreenState.Render(
                                        PlaceStateScreen.SuccessPlaceStats(state.data.toPlaceUI())
                                    )
                                )
                            is MenuItemViewType.PieChart ->
                                screenStateQueue.postValue(
                                    ScreenState.Render(
                                        PlaceStateScreen.SuccessPlaceAndStatsPieChart(state.data.toPlaceChartUI())
                                    )
                                )
                        }
                    is ListSubRegionStats ->
                        when (viewType) {
                            is MenuItemViewType.List ->
                                screenStateQueue.postValue(
                                    ScreenState.Render(
                                        PlaceStateScreen.SuccessPlaceStats(state.data.toPlaceUI())
                                    )
                                )
                            is MenuItemViewType.PieChart ->
                                screenStateQueue.postValue(
                                    ScreenState.Render(
                                        PlaceStateScreen.SuccessPlaceAndStatsPieChart(state.data.toPlaceChartUI())
                                    )
                                )
                        }
                    is ListCountryOnlyStats ->
                        screenStateQueue.postValue(
                            ScreenState.Render(
                                PlaceStateScreen.SuccessPlaceTotalStatsBarChart(state.data.toPlaceUI())
                            )
                        )
                    is ListRegionOnlyStats ->
                        screenStateQueue.postValue(
                            ScreenState.Render(
                                PlaceStateScreen.SuccessPlaceTotalStatsBarChart(state.data.toPlaceUI())
                            )
                        )
                    is ListRegionAndStats ->
                        when (viewType) {
                            is MenuItemViewType.BarChart ->
                                screenStateQueue.postValue(
                                    ScreenState.Render(
                                        PlaceStateScreen.SuccessPlaceStatsBarChart(state.data.toPlaceUI())
                                    )
                                )
                        }
                    is ListSubRegionAndStats ->
                        when (viewType) {
                            is MenuItemViewType.BarChart ->
                                screenStateQueue.postValue(
                                    ScreenState.Render(
                                        PlaceStateScreen.SuccessPlaceStatsBarChart(state.data.toPlaceUI())
                                    )
                                )
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

                        screenStateQueue.postValue(
                            ScreenState.Render(
                                PlaceStateScreen.SuccessPlaceStatsLineCharts(mapPlacesLineStats)
                            )
                        )
                    }
                }
            }
            is State.Loading ->
                screenStateQueue.postValue(ScreenState.Loading)
            is State.EmptyData ->
                screenStateQueue.postValue(ScreenState.EmptyData)
        }
    }

    private fun handleError(state: StateError<DomainError>) {
        when (state) {
            is StateError.Error ->
                screenStateQueue.postValue(
                    ScreenState.Error(
                        PlaceStateScreen.SomeError(state.error.toUI())
                    )
                )
        }
    }

    private fun cancelAllCharts() {
        jobListOrPieChart?.cancel()
        jobBarChart?.cancel()
        jobLineChart?.cancel()
    }
}