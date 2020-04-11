package com.jaimegc.covid19tracker.data.repository

import androidx.annotation.MainThread
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.flow.flow

abstract class NetworkRepository<E: DomainError, T> {

    fun asFlowEither() = flow<Either<StateError<E>, State<T>>> {
        emit(Either.right(State.Loading()))

        val apiResponse = fetchFromRemoteEither()

        apiResponse.fold({ error ->
            emit(Either.left(StateError.Error(error)))
        }, { success ->
            emit(Either.right(State.Success(success)))
        })
    }

    /*fun asFlow() = flow<State<T>> {

        // Emit Loading State
        emit(State.Loading())

        try {
            val apiResponse = fetchFromRemote()

            // Parse body
            val remotePosts = apiResponse.body()

            // Check for response validation
            if (apiResponse.isSuccessful && remotePosts != null) {
                emit(State.Success(remotePosts))
            } else {
                // Something went wrong! Emit Error state.
                emit(State.Error(DomainError.NoInternetDomainError))
            }
        } catch (e: Exception) {
            // Exception occurred! Emit error
            emit(State.Error(DomainError.NoInternetDomainError))
            e.printStackTrace()
        }
    }

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<T>*/

    @MainThread
    protected abstract suspend fun fetchFromRemoteEither(): Either<DomainError, T>
}