package com.jaimegc.covid19tracker.data.repository

import androidx.annotation.MainThread
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.flow.flow

interface BaseRepository<E: DomainError, T> {

    fun asFlow(policy: CachePolicy = CachePolicy.LocalFirst) = flow<Either<StateError<E>, State<T>>> {
        emit(Either.right(State.Loading()))

        val datasources = mutableListOf<Datasource>()

        when (policy) {
            CachePolicy.NetworkOnly -> datasources.add(Datasource.Network)
            CachePolicy.LocalOnly -> datasources.add(Datasource.Local)
            CachePolicy.LocalFirst -> datasources.addAll(listOf(Datasource.Local, Datasource.Network))
        }

        datasources.map { ds ->
            val response =
                when (ds) {
                    is Datasource.Network -> fetchFromRemote()
                    is Datasource.Local -> fetchFromLocal()
                }

            response.fold({ error ->
                emit(Either.left(StateError.Error(error)))
            }, { success ->
                emit(Either.right(State.Success(success)))
            })
        }
    }

    @MainThread
    suspend fun fetchFromLocal(): Either<DomainError, T> = Either.left(DomainError.LocalDatasourceNotImplemented)

    @MainThread
    suspend fun fetchFromRemote(): Either<DomainError, T> = Either.left(DomainError.RemoteDatasourceNotImplemented)

    private sealed class Datasource {
        object Local : Datasource()
        object Network : Datasource()
    }
}