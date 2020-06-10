package com.jaimegc.covid19tracker.ui.base

import androidx.fragment.app.Fragment
import com.jaimegc.covid19tracker.ui.base.states.BaseScreenState
import com.jaimegc.covid19tracker.ui.base.states.BaseViewScreenState
import org.koin.core.KoinComponent

abstract class BaseFragment<T: BaseScreenStateViewModel<S>, S: BaseScreenState>(
    layoutRes: Int
) : Fragment(layoutRes), BaseViewScreenState<T, S>, KoinComponent {

    protected val MENU_ITEM_LIST = 0
    protected val MENU_ITEM_BAR_CHART = 1
    protected val MENU_ITEM_LINE_CHART = 2
    protected val MENU_ITEM_PIE_CHART = 3
}