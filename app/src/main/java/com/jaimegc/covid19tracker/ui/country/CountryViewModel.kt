package com.jaimegc.covid19tracker.ui.country

import androidx.lifecycle.*
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    fun getCountries() =
        viewModelScope.launch {
            getCountry.getCountries().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it) }
                )
            }
        }

    fun getListChartStats(idCountry: String, idRegion: String = "") =
        listOrPieChartStats(idCountry, idRegion, MenuItemViewType.List)

    fun getPieChartStats(idCountry: String, idRegion: String = "") =
        listOrPieChartStats(idCountry, idRegion, MenuItemViewType.PieChart)

    private fun listOrPieChartStats(idCountry: String, idRegion: String, viewType: MenuItemViewType) =
        viewModelScope.launch {
            val date = "2020-05-24"

            if (idRegion.isEmpty()) {
                val countryAndStatsByDate =
                    getCountryStats.getCountryAndStatsByDate(idCountry, date)
                val regionsStatsOrderByConfirmed =
                    getRegionStats.getRegionsStatsOrderByConfirmed(idCountry, date)

                countryAndStatsByDate.combine(regionsStatsOrderByConfirmed) { countries, regions ->
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

                regionAndStatsByDate.combine(subRegionsStatsOrderByConfirmed) { regions, subRegions ->
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

    fun getRegionsByCountry(idCountry: String) =
        viewModelScope.launch {
            getRegion.getRegionsByCountry(idCountry).collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it) }
                )
            }
        }

    fun getBarChartStats(idCountry: String, idRegion: String = "") =
        viewModelScope.launch {

            if (idRegion.isEmpty()) {
                val countryStats = getCountryStats.getCountryAllStats(idCountry)
                val regionStats = getRegionStats.getRegionsAllStatsOrderByConfirmed(idCountry)

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

    fun getLineChartStats(idCountry: String, idRegion: String = "") =
        viewModelScope.launch {
            mapPlacesLineStats.clear()

            if (idRegion.isEmpty()) {
                val regionsMostConfirmed = getRegionStats.getRegionsAndStatsWithMostConfirmed(idCountry)
                val regionsMostDeaths = getRegionStats.getRegionsAndStatsWithMostDeaths(idCountry)
                val regionsMostRecovered = getRegionStats.getRegionsAndStatsWithMostRecovered(idCountry)
                val regionsMostOpenCases = getRegionStats.getRegionsAndStatsWithMostOpenCases(idCountry)

                combineFlows(regionsMostConfirmed, regionsMostDeaths, regionsMostRecovered,
                    regionsMostOpenCases).collect { results ->
                    results.mapIndexed { index, result ->
                        val viewType =
                            when (index) {
                                1 -> MenuItemViewType.LineChartMostConfirmed
                                2 -> MenuItemViewType.LineChartMostDeaths
                                3 -> MenuItemViewType.LineChartMostRecovered
                                else -> MenuItemViewType.LineChartMostOpenCases
                            }

                        result.fold(
                            { handleError(it) },
                            { handleState(state = it, viewType = viewType) }
                        )
                    }
                }
            } else {
                val subRegionsMostConfirmed =
                    getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(idCountry, idRegion)
                val subRegionsMostDeaths =
                    getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(idCountry, idRegion)
                val subRegionsMostRecovered =
                    getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(idCountry, idRegion)
                val subRegionsMostOpenCases =
                    getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(idCountry, idRegion)

                combineFlows(subRegionsMostConfirmed, subRegionsMostDeaths, subRegionsMostRecovered,
                    subRegionsMostOpenCases).collect { results ->
                    results.mapIndexed { index, result ->
                        val viewType =
                            when (index) {
                                1 -> MenuItemViewType.LineChartMostConfirmed
                                2 -> MenuItemViewType.LineChartMostDeaths
                                3 -> MenuItemViewType.LineChartMostRecovered
                                else -> MenuItemViewType.LineChartMostOpenCases
                            }

                        result.fold(
                            { handleError(it) },
                            { handleState(state = it, viewType = viewType) }
                        )
                    }
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
                    is ListRegionAndStats -> {
                        when (viewType) {
                            is MenuItemViewType.BarChart ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessPlaceStatsBarChart(state.data.toPlaceUI())))
                            is MenuItemViewType.LineChartMostConfirmed,
                               MenuItemViewType.LineChartMostDeaths,
                               MenuItemViewType.LineChartMostOpenCases,
                               MenuItemViewType.LineChartMostRecovered -> {
                                   mapPlacesLineStats[viewType] = state.data.toPlaceUI()

                                   if (viewType == MenuItemViewType.LineChartMostRecovered) {
                                       _screenState.postValue(ScreenState.Render(
                                           PlaceStateScreen.SuccessRegionsStatsLineCharts(mapPlacesLineStats)))
                                   }
                            }
                        }
                    }
                    is ListSubRegionAndStats -> {
                        when (viewType) {
                            is MenuItemViewType.BarChart ->
                                _screenState.postValue(ScreenState.Render(
                                    PlaceStateScreen.SuccessPlaceStatsBarChart(state.data.toPlaceUI())))
                            is MenuItemViewType.LineChartMostConfirmed,
                               MenuItemViewType.LineChartMostDeaths,
                               MenuItemViewType.LineChartMostOpenCases,
                               MenuItemViewType.LineChartMostRecovered -> {
                                   mapPlacesLineStats[viewType] = state.data.toPlaceUI()

                                   if (viewType == MenuItemViewType.LineChartMostRecovered) {
                                       _screenState.postValue(ScreenState.Render(
                                           PlaceStateScreen.SuccessRegionsStatsLineCharts(mapPlacesLineStats)))
                                }
                            }
                        }
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

    }
}