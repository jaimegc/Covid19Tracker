package com.jaimegc.covid19tracker.viewmodel.kotest

import app.cash.turbine.test
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.ui.home.MainViewModel
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerLoading
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.olog.flow.test.observer.test
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.FunSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow

class MainViewModelKotestTest : FunSpec({

    lateinit var mainViewModel: MainViewModel
    val getCovidTracker: GetCovidTracker = mock()

    beforeTest {
        mainViewModel = MainViewModel(getCovidTracker)
    }

    /**************
     *  Flow Test *
     **************/

    test("get covid tracker by date should return loading and success if date exists using flow test") {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidTrackerSuccess))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        mainViewModel.getCovidTracker()

        getCovidTracker.getCovidTrackerByDate().test(this) {
            valueAt(0).shouldBeRight(stateCovidTrackerLoading)
            valueAt(1).shouldBeRight(stateCovidTrackerSuccess)
            assertValueCount(2)
            assertComplete()
        }
    }
    
    test("get covid tracker by date with empty data should return loading and empty success using flow test") {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        mainViewModel.getCovidTracker()

        getCovidTracker.getCovidTrackerByDate().test(this) {
            valueAt(0).shouldBeRight(stateCovidTrackerLoading)
            valueAt(1).shouldBeRight(stateCovidTrackerEmptyData)
            assertValueCount(2)
            assertComplete()
        }
    }

    test("get covid tracker by date with database problem should return loading and unknown database error using flow test") {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        mainViewModel.getCovidTracker()

        getCovidTracker.getCovidTrackerByDate().test(this) {
            valueAt(0).shouldBeRight(stateCovidTrackerLoading)
            valueAt(1).shouldBeLeft(stateErrorUnknownDatabase)
            assertValueCount(2)
            assertComplete()
        }
    }

    /*********************
     *  Mockito Kotlin 2 *
     *********************/

    test("get covid tracker by date should return loading and success if date exists") {
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
                0 -> data.shouldBeRight(stateCovidTrackerLoading)
                1 -> data.shouldBeRight(stateCovidTrackerSuccess)
            }
        }
    }

    test("get covid tracker by date with empty data should return loading and empty success") {
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
                0 -> data.shouldBeRight(stateCovidTrackerLoading)
                1 -> data.shouldBeRight(stateCovidTrackerEmptyData)
            }
        }
    }

    test("get covid tracker by date with database problem should return loading and unknown database error") {
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
                0 -> data.shouldBeRight(stateCovidTrackerLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    /************
     *  Turbine *
     ************/

    test("get covid tracker by date should return loading and success if date exists using turbine") {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidTrackerSuccess))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        mainViewModel.getCovidTracker()

        getCovidTracker.getCovidTrackerByDate().test {
            expectItem().shouldBeRight(stateCovidTrackerLoading)
            expectItem().shouldBeRight(stateCovidTrackerSuccess)
            expectComplete()
        }
    }

    test("get covid tracker by date with empty data should return loading and empty success using turbine") {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        mainViewModel.getCovidTracker()

        getCovidTracker.getCovidTrackerByDate().test {
            expectItem().shouldBeRight(stateCovidTrackerLoading)
            expectItem().shouldBeRight(stateCovidTrackerEmptyData)
            expectComplete()
        }
    }

    test("get covid tracker by date with database problem should return loading and unknown database error using turbine") {
        val flow: Flow<Either<StateError<DomainError>, State<CovidTracker>>> = flow {
            emit(Either.right(stateCovidTrackerLoading))
            delay(10)
            emit(Either.left(stateErrorUnknownDatabase))
        }

        whenever(getCovidTracker.getCovidTrackerByDate()).thenReturn(flow)

        mainViewModel.getCovidTracker()

        getCovidTracker.getCovidTrackerByDate().test {
            expectItem().shouldBeRight(stateCovidTrackerLoading)
            expectItem().shouldBeLeft(stateErrorUnknownDatabase)
            expectComplete()
        }
    }
})