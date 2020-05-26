package com.jaimegc.covid19tracker.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.jaimegc.covid19tracker.common.QueueLiveData
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
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
     *  Adapted from
     *  https://stackoverflow.com/questions/61185082/combine-multiple-kotlin-flows-in-a-list-without-waiting-for-a-first-value
     */
    fun <T, E: DomainError> combineFlows(
        vararg flows: Flow<Either<StateError<E>, State<T>>>
    ): Flow<List<Either<StateError<E>, State<T>>>> = channelFlow {
        val flowsSize = flows.size
        val array = Array(flowsSize) { false to (null as Either<StateError<E>, State<T>>?) }

        flows.forEachIndexed { index, flow ->
            launch {
                flow.collect { emittedElement ->
                    array[index] = true to emittedElement

                    array.firstOrNull() { it.first.not() } ?: run {
                        var loading = 0
                        var success = 0
                        var empty = 0
                        var errors = 0

                        array.map { pair ->

                            pair.second!!.fold (
                                {
                                    errors++
                                },
                                { state ->
                                    when (state) {
                                        is State.Loading -> loading++
                                        is State.Success -> success++
                                        is State.EmptyData -> empty++
                                    }
                                }
                            )
                        }

                        if (loading + errors == flowsSize || success + errors == flowsSize ||
                            empty  + errors == flowsSize) {
                            send(array.filter { it.first }.map { it.second!! })
                        }
                    }
                }
            }
        }
    }
}