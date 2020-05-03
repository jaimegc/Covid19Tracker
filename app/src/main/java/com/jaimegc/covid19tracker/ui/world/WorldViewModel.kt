package com.jaimegc.covid19tracker.ui.world

import androidx.lifecycle.*
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
    private val getCountryStats: GetCountryStats,
    private val savedStateHandle: SavedStateHandle
) : BaseScreenStateViewModel<WorldStateScreen>() {

    companion object {
        private val MENU_SAVED_DEFAULT = Menu.ListView.toString()
        private const val MENU_SAVED_STATE_KEY = "MENU_SAVED_STATE_KEY"
    }

    override val _screenState = MutableLiveData<ScreenState<WorldStateScreen>>()
    override val screenState: LiveData<ScreenState<WorldStateScreen>> = _screenState

    private val mapWorldLineStats =
        mutableMapOf<WorldStateCountriesStatsLineChartType, List<CountryListStatsChartUI>>()

    private val lineChartTypeSize = WorldStateCountriesStatsLineChartType::class.nestedClasses.size

    fun getCovidTrackerLast(type: CovidTrackerType) =
        viewModelScope.launch {
            when (type) {
                CovidTrackerType.Normal -> setMenu(Menu.ListView)
                CovidTrackerType.PieChart -> setMenu(Menu.PieChartView)
            }
            getCovidTrackerLast.getCovidTrackerByDate("2020-05-01").collect { result ->
                val showResult =
                    when (type) {
                        CovidTrackerType.Normal -> isCurrentMenu(Menu.ListView)
                        CovidTrackerType.PieChart -> isCurrentMenu(Menu.PieChartView)
                    }

                if (showResult) {
                    result.fold(
                        { handleError(it) },
                        { handleScreenStateCovidTracker(it, type) }
                    )
                }
            }
        }

    fun getWorldAllStats() =
        viewModelScope.launch {
            setMenu(Menu.BarChartView)
            getWorldStats.getWorldAllStats().collect { result ->
                if (isCurrentMenu(Menu.BarChartView)) {
                    result.fold(::handleError, ::handleScreenStateWorldStats)
                }
            }
        }

    fun getCountriesStatsOrderByConfirmed() =
        viewModelScope.launch {
            getCountryStats.getCountriesStatsOrderByConfirmed().collect { result ->
                if (isCurrentMenu(Menu.BarChartView)) {
                    result.fold(::handleError, ::handleScreenStateCountriesBarStats)
                }
            }
        }

    fun getWorldMostStats() {
        mapWorldLineStats.clear()
        setMenu(Menu.LineChartView)
        getCountriesAndStatsWithMostConfirmed()
        getCountriesAndStatsWithMostDeaths()
        getCountriesAndStatsWithMostRecovered()
        getCountriesAndStatsWithMostOpenCases()
    }

    private fun getCountriesAndStatsWithMostConfirmed() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostConfirmed().collect { result ->
                if (isCurrentMenu(Menu.LineChartView)) {
                    result.fold(
                        { handleError(it) },
                        { handleScreenStateCountriesLineStats(it, WorldStateCountriesStatsLineChartType.MostConfirmed) }
                    )
                }
            }
        }

    private fun getCountriesAndStatsWithMostDeaths() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostDeaths().collect { result ->
                if (isCurrentMenu(Menu.LineChartView)) {
                    result.fold(
                        { handleError(it) },
                        { handleScreenStateCountriesLineStats(it, WorldStateCountriesStatsLineChartType.MostDeaths) }
                    )
                }
            }
        }

    private fun getCountriesAndStatsWithMostRecovered() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostRecovered().collect { result ->
                if (isCurrentMenu(Menu.LineChartView)) {
                    result.fold(
                        { handleError(it) },
                        { handleScreenStateCountriesLineStats(it, WorldStateCountriesStatsLineChartType.MostRecovered) }
                    )
                }
            }
        }

    private fun getCountriesAndStatsWithMostOpenCases() =
        viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostOpenCases().collect { result ->
                if (isCurrentMenu(Menu.LineChartView)) {
                    result.fold(
                        { handleError(it) },
                        { handleScreenStateCountriesLineStats(it, WorldStateCountriesStatsLineChartType.MostOpenCases) }
                    )
                }
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

    private fun getSavedMenu(): MutableLiveData<String> =
        savedStateHandle.getLiveData(MENU_SAVED_STATE_KEY, MENU_SAVED_DEFAULT)

    private fun isCurrentMenu(menu: Menu): Boolean =
        getSavedMenu().value == menu.toString()

    private fun setMenu(menu: Menu) =
        savedStateHandle.set(MENU_SAVED_STATE_KEY, menu.toString())

    private sealed class Menu {
        object ListView : Menu()
        object BarChartView : Menu()
        object LineChartView : Menu()
        object PieChartView : Menu()
    }
}