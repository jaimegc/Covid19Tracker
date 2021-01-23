package com.jaimegc.covid19tracker.usecase

import com.jaimegc.covid19tracker.ModelFactoryTest.covidTracker
import com.jaimegc.covid19tracker.domain.usecase.AddCovidTracker
import com.jaimegc.covid19tracker.util.UseCaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class AddCovidTrackerTest : UseCaseTest() {

    private lateinit var addCovidTracker: AddCovidTracker

    @Before
    fun setup() {
        init()
        addCovidTracker = AddCovidTracker(repository)
    }

    @Test
    fun `add covid trackers should return unit`() = runBlockingTest {
        coEvery { repository.addCovidTrackers(any()) } returns Unit

        addCovidTracker.addCovidTrackers(listOf(covidTracker))

        coVerify { repository.addCovidTrackers(any()) }
    }
}

