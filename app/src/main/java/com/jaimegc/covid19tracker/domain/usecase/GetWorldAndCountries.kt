package com.jaimegc.covid19tracker.domain.usecase

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class GetWorldAndCountries(
    private val repository: CovidTrackerRepository
) {

    fun getWorldAndCountriesByDate(
        date: String = ""
    ): Flow<Either<StateError<DomainError>, State<CovidTracker>>> =
        repository.getWorldAndCountriesByDate(date)
}