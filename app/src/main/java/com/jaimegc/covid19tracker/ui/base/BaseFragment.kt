package com.jaimegc.covid19tracker.ui.base

import androidx.fragment.app.Fragment
import com.jaimegc.covid19tracker.ui.base.states.BaseScreenState
import com.jaimegc.covid19tracker.ui.base.states.BaseViewScreenState
import org.koin.core.KoinComponent

abstract class BaseFragment<T : BaseScreenStateViewModel<S>, S : BaseScreenState>(
    layoutRes: Int
) : Fragment(layoutRes), BaseViewScreenState<T, S>, KoinComponent {

    protected val menuItemList = 0
    protected val menuItemBarChart = 1
    protected val menuItemLineChart = 2
    protected val menuItemPieChart = 3
}