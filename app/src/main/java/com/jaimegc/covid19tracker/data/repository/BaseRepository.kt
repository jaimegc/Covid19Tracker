package com.jaimegc.covid19tracker.data.repository

import arrow.core.Either
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.flow.*

interface BaseRepository<E : DomainError, T> {

    fun asFlow(
        policy: CachePolicy = CachePolicy.LocalOnly,
        loading: Boolean = true
    ) = flow<Either<StateError<E>, State<T>>> {
        if (loading || policy is CachePolicy.LocalFirst) emit(Either.right(State.Loading()))

        val datasources = mutableListOf<Datasource>()

        when (policy) {
            CachePolicy.LocalOnly ->
                datasources.add(Datasource.Local)
            is CachePolicy.LocalFirst ->
                if (policy.isCacheExpired) datasources.addAll(listOf(Datasource.Network, Datasource.Local))
        }

        datasources.map { ds ->
            when (ds) {
                is Datasource.Network ->
                    fetchFromRemote()
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

    fun fetchFromLocal(): Flow<Either<DomainError, T>>

    suspend fun fetchFromRemote() = Unit

    private sealed class Datasource {
        object Local : Datasource()
        object Network : Datasource()
    }
}