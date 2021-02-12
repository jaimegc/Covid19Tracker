package com.jaimegc.covid19tracker.usecase.kotest

import com.jaimegc.covid19tracker.ModelFactoryTest.covidTracker
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.usecase.AddCovidTracker
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class AddCovidTrackerKotestTest : ShouldSpec({

    lateinit var addCovidTracker: AddCovidTracker
    val repository: CovidTrackerRepository = mockk()

    beforeTest {
        MockKAnnotations.init(this)
        addCovidTracker = AddCovidTracker(repository)
    }

    should("add covid trackers should return unit") {
        coEvery { repository.addCovidTrackers(any()) } returns Unit

        addCovidTracker.addCovidTrackers(listOf(covidTracker))

        coVerify { repository.addCovidTrackers(any()) }
    }
})

