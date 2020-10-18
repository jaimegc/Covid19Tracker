package com.jaimegc.covid19tracker.viewmodel

import arrow.core.Either
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.ui.home.MainViewModel
import com.jaimegc.covid19tracker.utils.ModelBuilder.covidTracker
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.olog.flow.test.observer.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private val getCovidTracker: GetCovidTracker = mock()

    private val stateLoading: State<CovidTracker> = State.Loading()
    private val stateErrorDatabaseEmpty: StateError<DomainError> = StateError.Error(DomainError.DatabaseEmptyData)
    private val stateSuccess: State<CovidTracker> = State.Success(covidTracker)

    @Before
    fun setup() {
        mainViewModel = MainViewModel(getCovidTracker)
    }

    @Test
    fun getCovidTrackerByDateReturnLoadingAndSuccess() = runBlockingTest {
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
    fun getCovidTrackerByDateReturnLoadingAndErrorDatabaseEmpty() = runBlockingTest {
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
}