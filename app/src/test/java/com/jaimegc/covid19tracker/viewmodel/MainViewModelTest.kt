package com.jaimegc.covid19tracker.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.ui.home.MainViewModel
import com.jaimegc.covid19tracker.util.MainCoroutineRule
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerLoading
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.olog.flow.test.observer.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineRule()

    private lateinit var mainViewModel: MainViewModel
    private val getCovidTracker: GetCovidTracker = mock()

    @Before
    fun setup() {
        mainViewModel = MainViewModel(getCovidTracker)
    }

    /**************
     *  Flow Test *
     **************/

    @Test
    fun `get covid tracker by date should return loading and success if date exists using flow test`() = runBlockingTest {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidTrackerSuccess))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        mainViewModel.getCovidTracker()

        getCovidTracker.getCovidTrackerByDate().test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.right(stateCovidTrackerSuccess))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get covid tracker by date with empty data should return loading and empty success using flow test`() = runBlockingTest {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        mainViewModel.getCovidTracker()

        getCovidTracker.getCovidTrackerByDate().test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.right(stateCovidTrackerEmptyData))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get covid tracker by date with database problem should return loading and unknown database error using flow test`() = runBlockingTest {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        mainViewModel.getCovidTracker()

        getCovidTracker.getCovidTrackerByDate().test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.left(stateErrorUnknownDatabase))
            assertValueCount(2)
            assertComplete()
        }
    }

    /*********************
     *  Mockito Kotlin 2 *
     *********************/

    @Test
    fun `get covid tracker by date should return loading and success if date exists`() = runBlockingTest {
        val useCase = mock<GetCovidTracker> {
            onBlocking { getCovidTrackerByDate() } doReturn flow {
                emit(Either.right(stateCovidTrackerLoading))
                delay(10)
                emit(Either.right(stateCovidTrackerSuccess))
            }
        }

        val flow = useCase.getCovidTrackerByDate()

        flow.collectIndexed { index, data ->
            when (index) {
                0 -> assertEquals(data, Either.right(stateCovidTrackerLoading))
                1 -> assertEquals(data, Either.right(stateCovidTrackerSuccess))
            }
        }
    }

    @Test
    fun `get covid tracker by date with empty data should return loading and empty success`() = runBlockingTest {
        val useCase = mock<GetCovidTracker> {
            onBlocking { getCovidTrackerByDate() } doReturn flow {
                emit(Either.right(stateCovidTrackerLoading))
                delay(10)
                emit(Either.right(stateCovidTrackerEmptyData))
            }
        }

        val flow = useCase.getCovidTrackerByDate()

        flow.collectIndexed { index, data ->
            when (index) {
                0 -> assertEquals(data, Either.right(stateCovidTrackerLoading))
                1 -> assertEquals(data, Either.right(stateCovidTrackerEmptyData))
            }
        }
    }

    @Test
    fun `get covid tracker by date with database problem should return loading and unknown database error`() = runBlockingTest {
        val useCase = mock<GetCovidTracker> {
            onBlocking { getCovidTrackerByDate() } doReturn flow {
                emit(Either.right(stateCovidTrackerLoading))
                delay(10)
                emit(Either.left(stateErrorUnknownDatabase))
            }
        }

        val flow = useCase.getCovidTrackerByDate()

        flow.collectIndexed { index, data ->
            when (index) {
                0 -> assertEquals(data, Either.right(stateCovidTrackerLoading))
                1 -> assertEquals(data, Either.left(stateErrorUnknownDatabase))
            }
        }
    }
}