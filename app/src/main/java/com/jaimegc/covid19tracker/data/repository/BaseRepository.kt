package com.jaimegc.covid19tracker.data.repository

import arrow.core.Either
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.common.extensions.Coroutines
import kotlinx.coroutines.flow.*

interface BaseRepository<E: DomainError, T> {

    fun asFlow(
        policy: CachePolicy = CachePolicy.LocalOnly
    ) = flow<Either<StateError<E>, State<T>>> {
        emit(Either.right(State.Loading()))

        val datasources = mutableListOf<Datasource>()

        when (policy) {
            CachePolicy.LocalOnly ->
                datasources.add(Datasource.Local)
            CachePolicy.LocalFirst ->
                datasources.addAll(listOf(Datasource.Network, Datasource.Local))
        }

        datasources.map { ds ->
            when (ds) {
                is Datasource.Network ->
                    Coroutines.io { fetchFromRemote() }
                is Datasource.Local ->
                    fetchFromLocal().collect { value ->
                        value.fold({ error ->
                            when (error) {
                                is DomainError.DatabaseEmptyData ->
                                    emit(Either.right(State.EmptyData()))
                                else ->
                                    emit(Either.left(StateError.Error(error)))
                            }
                        }, { success ->
                            emit(Either.right(State.Success(success)))
                        })
                }
            }
        }
    }

    suspend fun fetchFromLocal(): Flow<Either<DomainError, T>>

    suspend fun fetchFromRemote() = Unit

    private sealed class Datasource {
        object Local : Datasource()
        object Network : Datasource()
    }
}