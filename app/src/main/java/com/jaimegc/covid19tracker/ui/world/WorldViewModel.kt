package com.jaimegc.covid19tracker.ui.world

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTrackerLast
import com.jaimegc.covid19tracker.domain.usecase.GetWorldStats
import com.jaimegc.covid19tracker.ui.model.CountryListStatsChartUI
import com.jaimegc.covid19tracker.ui.model.toListChartUI
import com.jaimegc.covid19tracker.ui.model.toUI
import com.jaimegc.covid19tracker.ui.states.CovidTrackerType
import com.jaimegc.covid19tracker.ui.viewmodel.BaseScreenStateViewModel
import com.jaimegc.covid19tracker.ui.states.ScreenState
import com.jaimegc.covid19tracker.ui.states.WorldStateCountriesStatsLineChartType
import com.jaimegc.covid19tracker.ui.states.WorldStateScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WorldViewModel(
    private val getCovidTrackerLast: GetCovidTrackerLast,
    private val getWorldStats: GetWorldStats,
    private val getCountryStats: GetCountryStats
) : BaseScreenStateViewModel<WorldStateScreen>() {

    override val _screenState = MutableLiveData<ScreenState<WorldStateScreen>>()
    override val screenState: LiveData<ScreenState<WorldStateScreen>> = _screenState

    private val mapWorldLineStats =
        mutableMapOf<WorldStateCountriesStatsLineChartType, List<CountryListStatsChartUI>>()

    private val lineChartTypeSize = WorldStateCountriesStatsLineChartType::class.nestedClasses.size

    fun getCovidTrackerLast(type: CovidTrackerType) =
        viewModelScope.launch {
            getCovidTrackerLast.getCovidTrackerByDate("2020-04-26").collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleScreenStateCovidTracker(it, type) }
                )
            }
        }

    fun getWorldAllStats() =
        viewModelScope.launch {
            getWorldStats.getWorldAllStats().collect { result ->
                result.fold(::handleError, ::handleScreenStateWorldStats)
            }
        }

    fun getCountriesStatsOrderByConfirmed() =
        viewModelScope.launch {
            getCountryStats.getCountriesStatsOrderByConfirmed().collect { result ->
                result.fold(::handleError, ::handleScreenStateCountriesBarStats)
            }
        }

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
                    { handleScreenStateCountriesLineStats(it, WorldStateCountriesStatsLineChartType.MostConfirmed) }
                )
            }
        }

    private fun getCountriesAndStatsWithMostDeaths() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostDeaths().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleScreenStateCountriesLineStats(it, WorldStateCountriesStatsLineChartType.MostDeaths) }
                )
            }
        }

    private fun getCountriesAndStatsWithMostRecovered() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostRecovered().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleScreenStateCountriesLineStats(it, WorldStateCountriesStatsLineChartType.MostRecovered) }
                )
            }
        }

    private fun getCountriesAndStatsWithMostOpenCases() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostOpenCases().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleScreenStateCountriesLineStats(it, WorldStateCountriesStatsLineChartType.MostOpenCases) }
                )
            }
        }

    private fun handleScreenStateCovidTracker(state: State<CovidTracker>, type: CovidTrackerType) =
        when (state) {
            is State.Success ->
                when (type) {
                    is CovidTrackerType.Normal ->
                        _screenState.postValue(ScreenState.Render(
                            WorldStateScreen.SuccessCovidTracker(state.data.toUI())))
                    is CovidTrackerType.PieChart ->
                        _screenState.postValue(ScreenState.Render(
                            WorldStateScreen.SuccessCountriesStatsPieCharts(state.data.toListChartUI())))
                }
            is State.Loading ->
                _screenState.postValue(ScreenState.Loading)
    }

    private fun handleScreenStateWorldStats(state: State<List<WorldStats>>) =
        when (state) {
            is State.Success ->
                _screenState.postValue(ScreenState.Render(WorldStateScreen.SuccessWorldStatsBarCharts(
                    state.data.map { worldStats -> worldStats.toListChartUI() })))
            is State.Loading ->
                _screenState.postValue(ScreenState.Loading)
        }

    private fun handleScreenStateCountriesBarStats(state: State<List<CountryListStats>>) =
        when (state) {
            is State.Success ->
                _screenState.postValue(ScreenState.Render(WorldStateScreen.SuccessCountriesStatsBarCharts(
                    state.data.map { countryStats -> countryStats.toListChartUI() })))
            is State.Loading ->
                _screenState.postValue(ScreenState.Loading)
        }

    private fun handleScreenStateCountriesLineStats(
        state: State<List<CountryListStats>>, lineChartType: WorldStateCountriesStatsLineChartType) {
        when (state) {
            is State.Success -> {
                mapWorldLineStats[lineChartType] = state.data.map { countryStats -> countryStats.toListChartUI() }

                if (mapWorldLineStats.size == lineChartTypeSize) {
                    _screenState.postValue(ScreenState.Render(
                        WorldStateScreen.SuccessCountriesStatsLineCharts(mapWorldLineStats)))
                }
            }
            is State.Loading ->
                _screenState.postValue(ScreenState.Loading)
        }
    }

    private fun handleError(state: StateError<DomainError>) {

    }
}