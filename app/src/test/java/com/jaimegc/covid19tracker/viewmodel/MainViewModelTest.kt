package com.jaimegc.covid19tracker.viewmodel

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.ui.home.MainViewModel
import com.jaimegc.covid19tracker.utils.ModelBuilder.covidTracker
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

    private val stateLoading: State<CovidTracker> = State.Loading()
    private val stateErrorDatabaseEmpty: StateError<DomainError> =
        StateError.Error(DomainError.DatabaseEmptyData)
    private val stateSuccess: State<CovidTracker> = State.Success(covidTracker)

    @Before
    fun setup() {
        getCovidTracker = GetCovidTracker(repository)
        mainViewModel = MainViewModel(getCovidTracker)
    }

    @Test
    fun getCovidTrackerByDateReturnLoadingAndSuccessIfDateExists() = runBlockingTest {
        val useCase = mock<GetCovidTracker>() {
            onBlocking { getCovidTrackerByDate() } doReturn flow {
                emit(Either.right(stateLoading))
                delay(10)
                emit(Either.right(stateSuccess))
            }
        }

        val flow = useCase.getCovidTrackerByDate()

        flow.collectIndexed { index, data ->
            when (index) {
                0 -> assertEquals(data, Either.right(stateLoading))
                1 -> assertEquals(data, Either.right(stateSuccess))
            }
        }
    }

    @Test
    fun getCovidTrackerByDateReturnLoadingAndErrorDatabaseEmptyIfDoesntDateExist() = runBlockingTest {
        val useCase = mock<GetCovidTracker>() {
            onBlocking { getCovidTrackerByDate() } doReturn flow {
                emit(Either.right(stateLoading))
                delay(10)
                emit(Either.left(stateErrorDatabaseEmpty))
            }
        }

        val flow = useCase.getCovidTrackerByDate()

        flow.collectIndexed { index, data ->
            when (index) {
                0 -> assertEquals(data, Either.right(stateLoading))
                1 -> assertEquals(data, Either.left(stateErrorDatabaseEmpty))
            }
        }
    }

    @Test
    fun getCovidTrackerByDateReturnLoadingAndSuccessIfDateExistsUsingFlowTest() = runBlockingTest {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateLoading))
            delay(10)
            emit(Either.right(stateSuccess))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        getCovidTracker.getCovidTrackerByDate().test(this) {
            assertValues(Either.right(stateLoading), Either.right(stateSuccess))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun getCovidTrackerByDateReturnLoadingAndErrorDatabaseEmptyIfDoesntDateExistUsingFlowTest() = runBlockingTest {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateLoading))
            delay(10)
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        getCovidTracker.getCovidTrackerByDate().test(this) {
            assertValues(Either.right(stateLoading), Either.left(stateErrorDatabaseEmpty))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun getCovidTrackerByDateShouldCallRepository() = runBlockingTest {
        getCovidTracker.getCovidTrackerByDate()

        verify(repository).getCovidTrackerByDate(any())
    }
}