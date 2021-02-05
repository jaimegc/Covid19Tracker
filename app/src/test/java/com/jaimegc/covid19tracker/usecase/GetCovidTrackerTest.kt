package com.jaimegc.covid19tracker.usecase

import app.cash.turbine.test
import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
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
    fun `get covid tracker by date with empty data should return loading empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateCovidTrackerEmptyData))
            }
        }
    }

    @Test
    fun `get covid tracker with database problem by date should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
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
    fun `get covid tracker by date with empty data should return loading empty success using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.right(stateCovidTrackerEmptyData))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get covid tracker with database problem by date should return loading and unknown database error using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.left(stateErrorUnknownDatabase))
            assertValueCount(2)
            assertComplete()
        }
    }

    /************
     *  Turbine *
     ************/

    @Test
    fun `get covid tracker by date should return loading and success if date exists using turbine`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerSuccess))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test {
            assertThat(Either.right(stateCovidTrackerLoading)).isEqualTo(expectItem())
            assertThat(Either.right(stateCovidTrackerSuccess)).isEqualTo(expectItem())
            expectComplete()
        }
    }

    @Test
    fun `get covid tracker by date with empty data should return loading empty success using turbine`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test {
            assertThat(Either.right(stateCovidTrackerLoading)).isEqualTo(expectItem())
            assertThat(Either.right(stateCovidTrackerEmptyData)).isEqualTo(expectItem())
            expectComplete()
        }
    }

    @Test
    fun `get covid tracker with database problem by date should return loading and unknown database error using turbine`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test {
            assertThat(Either.right(stateCovidTrackerLoading)).isEqualTo(expectItem())
            assertThat(Either.left(stateErrorUnknownDatabase)).isEqualTo(expectItem())
            expectComplete()
        }
    }
}