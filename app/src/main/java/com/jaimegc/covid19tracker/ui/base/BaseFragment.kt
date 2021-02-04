package com.jaimegc.covid19tracker.ui.base

import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.jaimegc.covid19tracker.common.extensions.enableItem
import com.jaimegc.covid19tracker.ui.base.states.BaseScreenState
import com.jaimegc.covid19tracker.ui.base.states.BaseViewScreenState
import com.jaimegc.covid19tracker.ui.base.states.BaseViewScreenStateFlow
import org.koin.core.component.KoinComponent

abstract class BaseFragment<T : BaseScreenStateViewModel<S>, S : BaseScreenState>(
    layoutRes: Int
) : Fragment(layoutRes), BaseViewScreenState<T, S>, KoinComponent {

    internal lateinit var menu: Menu

    protected val menuItemList = 0
    protected val menuItemBarChart = 1
    protected val menuItemLineChart = 2
    protected val menuItemPieChart = 3

    fun configureToolbar(toolbar: Toolbar, titleRes: Int, menuRes: Int) {
        toolbar.title = getString(titleRes)
        toolbar.inflateMenu(menuRes)
        menu = toolbar.menu
        menu.enableItem(menuItemList)
    }
}

abstract class BaseFragmentStateFlow<T : BaseScreenStateViewModelStateFlow<S>, S : BaseScreenState>(
    layoutRes: Int
) : Fragment(layoutRes), BaseViewScreenStateFlow<T, S>, KoinComponent {

    internal lateinit var menu: Menu

    protected val menuItemList = 0
    protected val menuItemBarChart = 1
    protected val menuItemLineChart = 2
    protected val menuItemPieChart = 3

    fun configureToolbar(toolbar: Toolbar, titleRes: Int, menuRes: Int) {
        toolbar.title = getString(titleRes)
        toolbar.inflateMenu(menuRes)
        menu = toolbar.menu
        menu.enableItem(menuItemList)
    }
}