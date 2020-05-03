package com.jaimegc.covid19tracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaimegc.covid19tracker.ui.states.BaseScreenState
import com.jaimegc.covid19tracker.ui.states.ScreenState

abstract class BaseScreenStateViewModel<T : BaseScreenState> : ViewModel() {
    abstract val _screenState: MutableLiveData<ScreenState<T>>
    abstract val screenState: LiveData<ScreenState<T>>
}