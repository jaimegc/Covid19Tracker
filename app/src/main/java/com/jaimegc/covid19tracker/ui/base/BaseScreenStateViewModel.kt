package com.jaimegc.covid19tracker.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jaimegc.covid19tracker.common.QueueLiveData
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.ui.base.states.BaseScreenState
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseScreenStateViewModel<T : BaseScreenState> : ViewModel() {
    abstract val screenStateQueue: QueueLiveData<ScreenState<T>>
    abstract val screenState: LiveData<ScreenState<T>>
}

abstract class BaseScreenStateMenuViewModel<T : BaseScreenState> : BaseScreenStateViewModel<T>() {
    protected abstract suspend fun <T> handleState(
        state: State<T>,
        viewType: MenuItemViewType = MenuItemViewType.List
    )
}

abstract class BaseScreenStateViewModelStateFlow<T : BaseScreenState> : ViewModel() {
    abstract val screenStateQueue: MutableStateFlow<ScreenState<T>>
    abstract val screenState: StateFlow<ScreenState<T>>
}

abstract class BaseScreenStateMenuViewModelStateFlow<T : BaseScreenState> : BaseScreenStateViewModelStateFlow<T>() {
    protected abstract suspend fun <T> handleState(
        state: State<T>,
        viewType: MenuItemViewType = MenuItemViewType.List
    )
}