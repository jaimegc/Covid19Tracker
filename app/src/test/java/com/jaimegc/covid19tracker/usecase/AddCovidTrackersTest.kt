package com.jaimegc.covid19tracker.usecase

import com.jaimegc.covid19tracker.ModelFactoryTest.covidTracker
import com.jaimegc.covid19tracker.domain.usecase.AddCovidTrackers
import com.jaimegc.covid19tracker.utils.UseCaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class AddCovidTrackersTest : UseCaseTest() {

    private lateinit var addCovidTrackers: AddCovidTrackers

    @Before
    fun setup() {
        init()
        addCovidTrackers = AddCovidTrackers(repository)
    }

    @Test
    fun `add covid trackers should return unit`() = runBlockingTest {
        coEvery { repository.addCovidTrackers(any()) } returns Unit

        addCovidTrackers.addCovidTrackers(listOf(covidTracker))

        coVerify { repository.addCovidTrackers(any()) }
    }
}

