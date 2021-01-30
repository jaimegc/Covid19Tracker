package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetWorldAndCountries
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.util.UseCaseTest
import dev.olog.flow.test.observer.test
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetWorldAndCountriesTest : UseCaseTest() {

    private lateinit var getWorldAndCountries: GetWorldAndCountries

    @Before
    fun setup() {
        init()
        getWorldAndCountries = GetWorldAndCountries(repository)
    }

    /**********
     *  Mockk *
     **********/

    @Test
    fun `get world and countries by date should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerSuccess))
        }

        every { repository.getWorldAndCountriesByDate(any()) } returns flow

        val flowUseCase = getWorldAndCountries.getWorldAndCountriesByDate()

        verify { repository.getWorldAndCountriesByDate(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerSuccess))
            }
        }
    }

    @Test
    fun `get world and countries by date with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        every { repository.getWorldAndCountriesByDate(any()) } returns flow

        val flowUseCase = getWorldAndCountries.getWorldAndCountriesByDate()

        verify { repository.getWorldAndCountriesByDate(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerEmptyData))
            }
        }
    }

    @Test
    fun `get world and countries by date with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getWorldAndCountriesByDate(any()) } returns flow

        val flowUseCase = getWorldAndCountries.getWorldAndCountriesByDate()

        verify { repository.getWorldAndCountriesByDate(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    /**************
     *  Flow Test *
     **************/

    @Test
    fun `get world and countries by date should return loading and success if date exists using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerSuccess))
        }

        every { repository.getWorldAndCountriesByDate(any()) } returns flow

        val flowUseCase = getWorldAndCountries.getWorldAndCountriesByDate()

        verify { repository.getWorldAndCountriesByDate(any()) }
        flowUseCase.test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.right(stateCovidTrackerSuccess))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get world and countries by date with empty data should return loading and empty success using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        every { repository.getWorldAndCountriesByDate(any()) } returns flow

        val flowUseCase = getWorldAndCountries.getWorldAndCountriesByDate()

        verify { repository.getWorldAndCountriesByDate(any()) }
        flowUseCase.test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.right(stateCovidTrackerEmptyData))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get world and countries by date with database problem should return loading and unknown database error using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getWorldAndCountriesByDate(any()) } returns flow

        val flowUseCase = getWorldAndCountries.getWorldAndCountriesByDate()

        verify { repository.getWorldAndCountriesByDate(any()) }
        flowUseCase.test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.left(stateErrorUnknownDatabase))
            assertValueCount(2)
            assertComplete()
        }
    }
}