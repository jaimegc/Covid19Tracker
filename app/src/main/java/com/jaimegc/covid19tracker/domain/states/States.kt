package com.jaimegc.covid19tracker.domain.states

import com.jaimegc.covid19tracker.domain.model.DomainError

sealed class State<T> {
    class Loading<T> : State<T>()
    class EmptyData<T> : State<T>()
    data class Success<T>(val data: T) : State<T>()
}

sealed class StateError<T : DomainError> {
    data class Error<T : DomainError>(val error: DomainError) : StateError<T>()
}