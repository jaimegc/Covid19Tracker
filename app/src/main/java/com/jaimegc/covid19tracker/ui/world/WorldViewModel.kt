package com.jaimegc.covid19tracker.ui.world

import androidx.lifecycle.*
import com.jaimegc.covid19tracker.common.QueueLiveData
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTrackerLast
import com.jaimegc.covid19tracker.domain.usecase.GetWorldStats
import com.jaimegc.covid19tracker.ui.model.CountryListStatsChartUI
import com.jaimegc.covid19tracker.ui.model.toListChartUI
import com.jaimegc.covid19tracker.ui.model.toUI
import com.jaimegc.covid19tracker.ui.states.*
import com.jaimegc.covid19tracker.ui.viewmodel.BaseScreenStateMenuViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorldViewModel(
    private val getCovidTrackerLast: GetCovidTrackerLast,
    private val getWorldStats: GetWorldStats,
    private val getCountryStats: GetCountryStats
) : BaseScreenStateMenuViewModel<WorldStateScreen>() {

    override val _screenState = QueueLiveData<ScreenState<WorldStateScreen>>()
    override val screenState: LiveData<ScreenState<WorldStateScreen>> = _screenState

    private val mapWorldLineStats =
        mutableMapOf<MenuItemViewType, List<CountryListStatsChartUI>>()

    fun getCovidTrackerLast(viewType: MenuItemViewType) =
        viewModelScope.launch {
            getCovidTrackerLast.getCovidTrackerByDate("2020-05-06").collect { result ->
                result.fold({ handleError(it) }, { handleState(state = it, viewType = viewType) })
            }
        }

    fun getWorldAllStats() =
        viewModelScope.launch {
            getWorldStats.getWorldAllStats().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.BarChart) }
                )
            }
        }

    fun getCountriesStatsOrderByConfirmed() =
        viewModelScope.launch {
            getCountryStats.getCountriesStatsOrderByConfirmed().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.BarChart) }
                )
            }
        }

    /**
     *  Waiting for all requests manually.
     *  In CountryViewModel you can see it using combine.
     */
    fun getWorldMostStats() {
        mapWorldLineStats.clear()
        getCountriesAndStatsWithMostConfirmed()
        getCountriesAndStatsWithMostDeaths()
        getCountriesAndStatsWithMostRecovered()
        getCountriesAndStatsWithMostOpenCases()
    }

    private fun getCountriesAndStatsWithMostConfirmed() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostConfirmed().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.LineChartMostConfirmed) }
                )
            }
        }

    private fun getCountriesAndStatsWithMostDeaths() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostDeaths().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.LineChartMostDeaths) }
                )
            }
        }

    private fun getCountriesAndStatsWithMostRecovered() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostRecovered().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.LineChartMostRecovered) }
                )
            }
        }

    private fun getCountriesAndStatsWithMostOpenCases() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostOpenCases().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.LineChartMostOpenCases) }
                )
            }
        }

    override suspend fun <T> handleState(
        state: State<T>,
        viewType: MenuItemViewType
    ) {
        when (state) {
            is State.Success -> {
                when (state.data) {
                    is CovidTracker -> {
                        when (viewType) {
                            is MenuItemViewType.List ->
                                _screenState.postValue(ScreenState.Render(
                                    WorldStateScreen.SuccessCovidTracker(state.data.toUI())))
                            is MenuItemViewType.PieChart ->
                                _screenState.postValue(ScreenState.Render(
                                    WorldStateScreen.SuccessCountriesStatsPieCharts(
                                        state.data.toListChartUI())))
                        }
                    }
                    is ListWorldStats ->
                        _screenState.postValue(ScreenState.Render(
                            WorldStateScreen.SuccessWorldStatsBarCharts(
                                state.data.worldStats.map { worldStats -> worldStats.toListChartUI() })))
                    is ListCountryStats -> {
                        when (viewType) {
                            is MenuItemViewType.BarChart ->
                                _screenState.postValue(ScreenState.Render(
                                    WorldStateScreen.SuccessCountriesStatsBarCharts(
                                        state.data.countriesStats.map { countryStats ->
                                            countryStats.toListChartUI() })))
                            is MenuItemViewType.LineChartMostConfirmed,
                               MenuItemViewType.LineChartMostDeaths,
                               MenuItemViewType.LineChartMostOpenCases,
                               MenuItemViewType.LineChartMostRecovered -> {
                                   mapWorldLineStats[viewType] = state.data.countriesStats.map {
                                           countryStats -> countryStats.toListChartUI()
                                   }

                                   if (mapWorldLineStats.size == LINE_CHARTS_VIEW_TYPES) {
                                       _screenState.postValue(ScreenState.Render(
                                           WorldStateScreen.SuccessCountriesStatsLineCharts(mapWorldLineStats)))
                                   }
                            }
                        }
                    }
                }
            }
            is State.Loading ->
                _screenState.postValue(ScreenState.Loading)
        }
    }

    private fun handleError(state: StateError<DomainError>) {

    }

    companion object {
        private const val LINE_CHARTS_VIEW_TYPES = 4
    }
}