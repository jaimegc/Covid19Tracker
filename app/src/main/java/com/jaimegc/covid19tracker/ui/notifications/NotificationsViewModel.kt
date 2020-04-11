package com.jaimegc.covid19tracker.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.toUI
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTrackerLast
import com.jaimegc.covid19tracker.ui.viewmodel.BaseScreenStateViewModel
import com.jaimegc.covid19tracker.ui.model.CovidTrackerUI
import com.jaimegc.covid19tracker.ui.states.ScreenState
import com.jaimegc.covid19tracker.ui.states.WorldTotalStateScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NotificationsViewModel(
    val getCovidTrackerLast: GetCovidTrackerLast
) : BaseScreenStateViewModel<WorldTotalStateScreen>() {

    private val _covidTracker = MutableLiveData<State<CovidTrackerUI>>()
    val covidTracker: LiveData<State<CovidTrackerUI>> = _covidTracker

    override val _screenState = MutableLiveData<ScreenState<WorldTotalStateScreen>>()
    override val screenState: LiveData<ScreenState<WorldTotalStateScreen>> = _screenState

    fun getCovidTrackerLast() =
        viewModelScope.launch {
            getCovidTrackerLast.getCovidTrackerLast().collect { result ->
                result.fold(::handleError, ::handleScreenState)
            }
        }

    private fun handleScreenState(state: State<CovidTracker>) =
        when (state) {
            is State.Success ->
                _screenState.postValue(ScreenState.Render(WorldTotalStateScreen.Success(state.data.toUI())))
            is State.Loading ->
                _screenState.postValue(ScreenState.Loading)
    }

    private fun handleError(state: StateError<DomainError>) {

    }
}