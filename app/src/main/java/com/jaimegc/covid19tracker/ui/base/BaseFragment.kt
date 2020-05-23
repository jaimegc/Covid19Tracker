package com.jaimegc.covid19tracker.ui.base

import androidx.fragment.app.Fragment
import com.jaimegc.covid19tracker.ui.states.BaseScreenState
import com.jaimegc.covid19tracker.ui.states.BaseViewScreenState
import com.jaimegc.covid19tracker.ui.viewmodel.BaseScreenStateViewModel
import org.koin.core.KoinComponent

abstract class BaseFragment<T: BaseScreenStateViewModel<S>, S: BaseScreenState>(
    layoutRes: Int
) : Fragment(layoutRes), BaseViewScreenState<T, S>, KoinComponent