package com.jaimegc.covid19tracker.ui.states

import com.jaimegc.covid19tracker.ui.viewmodel.BaseScreenStateViewModel

interface BaseViewScreenState<T: BaseScreenStateViewModel<S>, S: BaseScreenState> {
    val viewModel: T
    fun handleRenderState(renderState: S)
}