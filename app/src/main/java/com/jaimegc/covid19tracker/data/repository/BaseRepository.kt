package com.jaimegc.covid19tracker.data.repository

import arrow.core.Either
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.extensions.Coroutines
import kotlinx.coroutines.flow.*

interface BaseRepository<E: DomainError, T> {

    fun asFlow(policy: CachePolicy = CachePolicy.LocalFirst) = flow<Either<StateError<E>, State<T>>> {
        emit(Either.right(State.Loading()))

        val datasources = mutableListOf<Datasource>()

        when (policy) {
            CachePolicy.LocalOnly -> datasources.add(Datasource.Local)
            CachePolicy.LocalFirst -> datasources.addAll(listOf(Datasource.Network, Datasource.Local))
        }

        datasources.map { ds ->
            when (ds) {
                is Datasource.Network ->
                    Coroutines.io { fetchFromRemote() }
                is Datasource.Local ->

                    fetchFromLocalState().collect { value ->
                        value.fold({ error ->
                            emit(Either.left(StateError.Error(error)))
                        }, { success ->
                            emit(Either.right(State.Success(success)))
                        })
                }
            }
        }
    }

    suspend fun fetchFromLocalState(): Flow<Either<DomainError, T>>

    suspend fun fetchFromRemote() = Unit

    private sealed class Datasource {
        object Local : Datasource()
        object Network : Datasource()
    }
}