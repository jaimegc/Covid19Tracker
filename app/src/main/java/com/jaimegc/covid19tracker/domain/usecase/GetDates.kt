package com.jaimegc.covid19tracker.domain.usecase

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.DomainError
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetDates(
    private val repository: CovidTrackerRepository
) {

    suspend fun getAllDates(): Either<DomainError, List<String>> =
        repository.getAllDates()
}