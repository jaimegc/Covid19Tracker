package com.jaimegc.covid19tracker.ui.base.states

import com.jaimegc.covid19tracker.ui.base.BaseScreenStateViewModel

interface BaseViewScreenState<T: BaseScreenStateViewModel<S>, S: BaseScreenState> {
    val viewModel: T
    fun handleRenderState(renderState: S)
}