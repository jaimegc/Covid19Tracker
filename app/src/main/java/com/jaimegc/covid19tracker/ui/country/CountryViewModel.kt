package com.jaimegc.covid19tracker.ui.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jaimegc.covid19tracker.domain.model.CountryOneStats
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.ListCountry
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
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
    private val getCountryStats: GetCountryStats
) : BaseScreenStateMenuViewModel<PlaceStateScreen>() {

    override val _screenState = MutableLiveData<ScreenState<PlaceStateScreen>>()
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

    fun getCountryAndStatsByIdDate(idCountry: String) =
        viewModelScope.launch {
            getCountryStats.getCountryAndStatsByIdDate(idCountry, "2020-05-06").collect { result ->
                result.fold(
                    { handleError(it) },
                    { handleState(state = it) }
                )
            }
        }

    override fun <T> handleState(
        state: State<T>,
        viewType: MenuItemViewType
    ) {
        when (state) {
            is State.Success -> {
                when (state.data) {
                    is ListCountry ->
                        _screenState.postValue(ScreenState.Render(PlaceStateScreen.SuccessSpinnerCountries(
                            state.data.countries.map { country -> country.toUI() })))
                    is CountryOneStats ->
                        _screenState.postValue(ScreenState.Render(PlaceStateScreen.SuccessPlaceStats(
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