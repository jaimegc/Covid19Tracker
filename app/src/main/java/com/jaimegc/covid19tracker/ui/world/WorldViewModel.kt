package com.jaimegc.covid19tracker.ui.world

import androidx.lifecycle.*
import com.jaimegc.covid19tracker.common.QueueLiveData
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetWorldAndCountries
import com.jaimegc.covid19tracker.domain.usecase.GetWorldStats
import com.jaimegc.covid19tracker.ui.model.CountryListStatsChartUI
import com.jaimegc.covid19tracker.ui.model.toListChartUI
import com.jaimegc.covid19tracker.ui.model.toUI
import com.jaimegc.covid19tracker.ui.base.states.*
import com.jaimegc.covid19tracker.ui.base.BaseScreenStateMenuViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class WorldViewModel(
    private val getWorldAndCountries: GetWorldAndCountries,
    private val getWorldStats: GetWorldStats,
    private val getCountryStats: GetCountryStats
) : BaseScreenStateMenuViewModel<WorldStateScreen>() {

    override val _screenState = QueueLiveData<ScreenState<WorldStateScreen>>()
    override val screenState: LiveData<ScreenState<WorldStateScreen>> = _screenState

    private val mapWorldLineStats =
        mutableMapOf<MenuItemViewType, List<CountryListStatsChartUI>>()

    private var jobWorldAndContries: Job? = null
    private var jobWorldAll: Job? = null
    private var jobCountriesConfirmed: Job? = null
    private var jobMostConfirmed: Job? = null
    private var jobMostDeaths: Job? = null
    private var jobMostOpenCases: Job? = null
    private var jobMostRecovered: Job? = null

    fun getListChartStats() =
        listOrPieChartStats(MenuItemViewType.List)

    fun getPieChartStats() =
        listOrPieChartStats(MenuItemViewType.PieChart)

    private fun listOrPieChartStats(viewType: MenuItemViewType) {
        cancelAll()
        jobWorldAndContries = viewModelScope.launch {
            val date = "" // Empty to get the last date or use yyyy-MM-dd
            getWorldAndCountries.getWorldAndCountriesByDate(date).collect { result ->
                result.fold({ handleError(it) }, { handleState(state = it, viewType = viewType) })
            }
        }
    }

    fun getBarChartStats() {
        cancelAll()
        getWorldAllStats()
        getCountriesStatsOrderByConfirmed()
    }

    private fun getWorldAllStats() {
        jobWorldAll = viewModelScope.launch {
            getWorldStats.getWorldAllStats().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.BarChart) }
                )
            }
        }
    }

    private fun getCountriesStatsOrderByConfirmed() {
        jobCountriesConfirmed = viewModelScope.launch {
            getCountryStats.getCountriesStatsOrderByConfirmed().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.BarChart) }
                )
            }
        }
    }

    /**
     *  Using four methods.
     *  In CountryViewModel you can see it using zip / combine / flatMapMerge.
     */
    fun getLineChartStats() {
        cancelAll()
        mapWorldLineStats.clear()
        getCountriesAndStatsWithMostConfirmed()
        getCountriesAndStatsWithMostDeaths()
        getCountriesAndStatsWithMostRecovered()
        getCountriesAndStatsWithMostOpenCases()
    }

    private fun getCountriesAndStatsWithMostConfirmed() {
        jobMostConfirmed = viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostConfirmed().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.LineChartMostConfirmed) }
                )
            }
        }
    }

    private fun getCountriesAndStatsWithMostDeaths() {
        jobMostDeaths = viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostDeaths().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.LineChartMostDeaths) }
                )
            }
        }
    }

    private fun getCountriesAndStatsWithMostRecovered() {
        jobMostRecovered = viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostRecovered().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.LineChartMostRecovered) }
                )
            }
        }
    }

    private fun getCountriesAndStatsWithMostOpenCases() {
        jobMostOpenCases = viewModelScope.launch {
            getCountryStats.getCountriesAndStatsWithMostOpenCases().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.LineChartMostOpenCases) }
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
                    is ListCountryAndStats -> {
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
                                _screenState.postValue(ScreenState.Render(
                                    WorldStateScreen.SuccessCountriesStatsLineCharts(mapWorldLineStats)))
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
        when (state) {
            is StateError.Error ->
                _screenState.postValue(ScreenState.Error(
                    WorldStateScreen.SomeError(state.error.toUI())))
        }
    }

    private fun cancelAll() {
        jobWorldAndContries?.cancel()
        jobWorldAll?.cancel()
        jobCountriesConfirmed?.cancel()
        jobMostConfirmed?.cancel()
        jobMostDeaths?.cancel()
        jobMostOpenCases?.cancel()
        jobMostRecovered?.cancel()
    }
}