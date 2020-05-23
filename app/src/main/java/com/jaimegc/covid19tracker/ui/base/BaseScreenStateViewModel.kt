package com.jaimegc.covid19tracker.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jaimegc.covid19tracker.common.QueueLiveData
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.ui.base.states.BaseScreenState
import com.jaimegc.covid19tracker.ui.base.states.MenuItemViewType
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseScreenStateViewModel<T : BaseScreenState> : ViewModel() {
    abstract val _screenState: QueueLiveData<ScreenState<T>>
    abstract val screenState: LiveData<ScreenState<T>>
}

abstract class BaseScreenStateMenuViewModel<T : BaseScreenState> : BaseScreenStateViewModel<T>() {
    protected abstract suspend fun <T> handleState(
        state: State<T>,
        viewType: MenuItemViewType = MenuItemViewType.List
    )

    /**
     *  https://stackoverflow.com/questions/61185082/combine-multiple-kotlin-flows-in-a-list-without-waiting-for-a-first-value
     */
    inline fun <reified T> combineFlows(vararg flows: Flow<T>): Flow<List<T>> = channelFlow {
        val array = Array(flows.size) { false to (null as T?) }

        flows.forEachIndexed { index, flow ->
            launch {
                flow.collect { emittedElement ->
                    array[index] = true to emittedElement!!
                    send(array.filter { it.first }.map { it.second!! })
                }
            }
        }
    }
}