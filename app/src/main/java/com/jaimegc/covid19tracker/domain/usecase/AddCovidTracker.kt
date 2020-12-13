package com.jaimegc.covid19tracker.domain.usecase

import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.CovidTracker

class AddCovidTracker(
    private val repository: CovidTrackerRepository
) {

    suspend fun addCovidTrackers(covidTrackers: List<CovidTracker>) =
        repository.addCovidTrackers(covidTrackers)
}