package com.jaimegc.covid19tracker.ui.states

import androidx.lifecycle.ViewModel

interface ViewScreenState<S: BaseScreenState, T: ViewModel> {
    val viewModel: T
    fun handleRenderState(renderState: S)
}