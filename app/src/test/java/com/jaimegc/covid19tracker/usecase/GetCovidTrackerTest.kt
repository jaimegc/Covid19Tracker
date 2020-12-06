package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
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

class GetCovidTrackerTest : UseCaseTest() {

    companion object {
        private const val DATE = "date"
    }

    private lateinit var getCovidTracker: GetCovidTracker

    @Before
    fun setup() {
        init()
        getCovidTracker = GetCovidTracker(repository)
    }

    /**********
     *  Mockk *
     **********/

    @Test
    fun `get covid tracker by date should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerSuccess))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerSuccess))
            }
        }
    }

    @Test
    fun `get covid tracker by date should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
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
    fun `get covid tracker by date should return loading and success if date exists using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerSuccess))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.right(stateCovidTrackerSuccess))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get covid tracker by date should return loading and error database empty if date doesnt exist using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.left(stateErrorDatabaseEmpty))
            assertValueCount(2)
            assertComplete()
        }
    }
}