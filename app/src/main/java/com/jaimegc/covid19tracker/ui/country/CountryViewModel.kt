package com.jaimegc.covid19tracker.ui.country

import androidx.lifecycle.*
import com.jaimegc.covid19tracker.common.QueueLiveData
import com.jaimegc.covid19tracker.domain.model.*
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetRegion
import com.jaimegc.covid19tracker.domain.usecase.GetRegionStats
import com.jaimegc.covid19tracker.ui.model.toPlaceUI
import com.jaimegc.covid19tracker.ui.model.toUI
import com.jaimegc.covid19tracker.ui.states.PlaceStateScreen
import com.jaimegc.covid19tracker.ui.states.MenuItemViewType
import com.jaimegc.covid19tracker.ui.states.ScreenState
import com.jaimegc.covid19tracker.ui.viewmodel.BaseScreenStateMenuViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountryViewModel(
    private val getCountry: GetCountry,
    private val getCountryStats: GetCountryStats,
    private val getRegion: GetRegion,
    private val getRegionStats: GetRegionStats
) : BaseScreenStateMenuViewModel<PlaceStateScreen>() {

    override val _screenState = QueueLiveData<ScreenState<PlaceStateScreen>>()
    override val screenState: LiveData<ScreenState<PlaceStateScreen>> = _screenState

    fun getCountries() =
        viewModelScope.launch {
            getCountry.getCountries().collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it) }
                )
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

    fun getCountryAndStatsByIdDate(idCountry: String) =
        viewModelScope.launch {
            getCountryStats.getCountryAndStatsByIdDate(idCountry, "2020-05-06").collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it) }
                )
            }
        }

    fun getRegionsStatsOrderByConfirmed(idCountry: String) =
        viewModelScope.launch {
            getRegionStats.getRegionsStatsOrderByConfirmed(idCountry, "2020-05-06").collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it, viewType = MenuItemViewType.BarChart) }
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
                    is ListCountry ->
                        _screenState.postValue(ScreenState.Render(PlaceStateScreen.SuccessSpinnerCountries(
                            state.data.countries.map { country -> country.toUI() })))
                    is ListRegion ->
                        _screenState.postValue(ScreenState.Render(PlaceStateScreen.SuccessSpinnerRegions(
                            state.data.regions.map { region -> region.toPlaceUI() })))
                    is CountryOneStats ->
                        _screenState.postValue(ScreenState.Render(PlaceStateScreen.SuccessCountryStats(
                            state.data.toPlaceUI())))
                    is ListRegionStats ->
                        _screenState.postValue(ScreenState.Render(PlaceStateScreen.SuccessRegionStats(
                            state.data.toPlaceUI())))
                }
            }
            is State.Loading ->
                _screenState.postValue(ScreenState.Loading)
        }
    }

    private fun handleError(state: StateError<DomainError>) {

    }
}