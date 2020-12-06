package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetWorldAndCountries
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCovidTrackerSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCovidTrackerLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateErrorDatabaseEmpty
import com.jaimegc.covid19tracker.utils.UseCaseTest
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
    fun `get world and countries by date should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getWorldAndCountriesByDate(any()) } returns flow

        val flowUseCase = getWorldAndCountries.getWorldAndCountriesByDate()

        verify { repository.getWorldAndCountriesByDate(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorDatabaseEmpty))
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
    fun `get world and countries by date should return loading and error database empty if date doesnt exist using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getWorldAndCountriesByDate(any()) } returns flow

        val flowUseCase = getWorldAndCountries.getWorldAndCountriesByDate()

        verify { repository.getWorldAndCountriesByDate(any()) }
        flowUseCase.test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.left(stateErrorDatabaseEmpty))
            assertValueCount(2)
            assertComplete()
        }
    }
}