package com.jaimegc.covid19tracker.viewmodel

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.ui.home.MainViewModel
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCovidSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateErrorDatabaseEmpty
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCovidTrackerLoading
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.olog.flow.test.observer.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var getCovidTracker: GetCovidTracker
    private val repository: CovidTrackerRepository = mock()

    @Before
    fun setup() {
        getCovidTracker = GetCovidTracker(repository)
        mainViewModel = MainViewModel(getCovidTracker)
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
                emit(Either.right(stateCovidSuccess))
            }
        }

        val flow = useCase.getCovidTrackerByDate()

        flow.collectIndexed { index, data ->
            when (index) {
                0 -> assertEquals(data, Either.right(stateCovidTrackerLoading))
                1 -> assertEquals(data, Either.right(stateCovidSuccess))
            }
        }
    }

    @Test
    fun `get covid tracker by date should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val useCase = mock<GetCovidTracker> {
            onBlocking { getCovidTrackerByDate() } doReturn flow {
                emit(Either.right(stateCovidTrackerLoading))
                delay(10)
                emit(Either.left(stateErrorDatabaseEmpty))
            }
        }

        val flow = useCase.getCovidTrackerByDate()

        flow.collectIndexed { index, data ->
            when (index) {
                0 -> assertEquals(data, Either.right(stateCovidTrackerLoading))
                1 -> assertEquals(data, Either.left(stateErrorDatabaseEmpty))
            }
        }
    }

    @Test
    fun `get covid tracker by date should call repository`() = runBlockingTest {
        getCovidTracker.getCovidTrackerByDate()

        verify(repository).getCovidTrackerByDate(any())
    }

    /**************
     *  Flow Test *
     **************/

    @Test
    fun `get covid tracker by date should return loading and success if date exists using flow test`() = runBlockingTest {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidSuccess))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        getCovidTracker.getCovidTrackerByDate().test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.right(stateCovidSuccess))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get covid tracker by date should return loading and error database empty if date doesnt exist using flow test`() = runBlockingTest {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        getCovidTracker.getCovidTrackerByDate().test(this) {
            assertValues(Either.right(stateCovidTrackerLoading), Either.left(stateErrorDatabaseEmpty))
            assertValueCount(2)
            assertComplete()
        }
    }
}