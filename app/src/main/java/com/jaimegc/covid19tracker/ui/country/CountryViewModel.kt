package com.jaimegc.covid19tracker.ui.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jaimegc.covid19tracker.domain.model.Country
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.ui.model.toUI
import com.jaimegc.covid19tracker.ui.states.CountryStateScreen
import com.jaimegc.covid19tracker.ui.states.ScreenState
import com.jaimegc.covid19tracker.ui.viewmodel.BaseScreenStateViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CountryViewModel(
    private val getCountry: GetCountry
) : BaseScreenStateViewModel<CountryStateScreen>() {

    override val _screenState = MutableLiveData<ScreenState<CountryStateScreen>>()
    override val screenState: LiveData<ScreenState<CountryStateScreen>> = _screenState

    fun getCountries() =
        viewModelScope.launch {
            getCountry.getCountries().collect { result ->
                result.fold(::handleError, ::handleScreenCountries)
            }
        }

    private fun handleScreenCountries(state: State<List<Country>>) =
        when (state) {
            is State.Success ->
                _screenState.postValue(ScreenState.Render(CountryStateScreen.SuccessCountries(
                    state.data.map { country -> country.toUI() })))
            is State.Loading ->
                _screenState.postValue(ScreenState.Loading)
        }

    private fun handleError(state: StateError<DomainError>) {

    }
}