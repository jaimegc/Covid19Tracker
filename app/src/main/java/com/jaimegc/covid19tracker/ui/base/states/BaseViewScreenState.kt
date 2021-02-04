package com.jaimegc.covid19tracker.ui.base.states

import com.jaimegc.covid19tracker.ui.base.BaseScreenStateViewModel
import com.jaimegc.covid19tracker.ui.base.BaseScreenStateViewModelStateFlow

interface BaseViewScreenState<T : BaseScreenStateViewModel<S>, S : BaseScreenState> {
    val viewModel: T
    fun handleRenderState(renderState: S)
}

interface BaseViewScreenStateFlow<T : BaseScreenStateViewModelStateFlow<S>, S : BaseScreenState> {
    val viewModel: T
    fun handleRenderState(renderState: S)
}