package com.jaimegc.covid19tracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.ui.states.BaseScreenState
import com.jaimegc.covid19tracker.ui.states.ScreenState

abstract class BaseScreenStateViewModel<T : BaseScreenState, S> : ViewModel() {
    abstract val _screenState: MutableLiveData<ScreenState<T>>
    abstract val screenState: LiveData<ScreenState<T>>
    abstract fun handleScreenState(state: State<S>)
    abstract fun handleError(state: StateError<DomainError>)
}