package com.jaimegc.covid19tracker.usecase.kotest

import app.cash.turbine.test
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.usecase.GetCovidTracker
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCovidTrackerLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import dev.olog.flow.test.observer.test
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow

class GetCovidTrackerKotestTest : ShouldSpec({

    val DATE = "date"

    lateinit var getCovidTracker: GetCovidTracker
    val repository: CovidTrackerRepository = mockk()

    beforeTest {
        MockKAnnotations.init(this)
        getCovidTracker = GetCovidTracker(repository)
    }

    /**********
     *  Mockk *
     **********/

    should("get covid tracker by date should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerSuccess))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateCovidTrackerLoading)
                1 -> data.shouldBeRight(stateCovidTrackerSuccess)
            }
        }
    }

    should("get covid tracker by date with empty data should return loading empty success") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateCovidTrackerLoading)
                1 -> data.shouldBeRight(stateCovidTrackerEmptyData)
            }
        }
    }

    should("get covid tracker with database problem by date should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateCovidTrackerLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    /**************
     *  Flow Test *
     **************/

    should("get covid tracker by date should return loading and success if date exists using flow test") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerSuccess))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test(this) {
            valueAt(0).shouldBeRight(stateCovidTrackerLoading)
            valueAt(1).shouldBeRight(stateCovidTrackerSuccess)
            assertValueCount(2)
            assertComplete()
        }
    }

    should("get covid tracker by date with empty data should return loading empty success using flow test") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test(this) {
            valueAt(0).shouldBeRight(stateCovidTrackerLoading)
            valueAt(1).shouldBeRight(stateCovidTrackerEmptyData)
            assertValueCount(2)
            assertComplete()
        }
    }

    should("get covid tracker with database problem by date should return loading and unknown database error using flow test") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test(this) {
            valueAt(0).shouldBeRight(stateCovidTrackerLoading)
            valueAt(1).shouldBeLeft(stateErrorUnknownDatabase)
            assertValueCount(2)
            assertComplete()
        }
    }

    /************
     *  Turbine *
     ************/

    should("get covid tracker by date should return loading and success if date exists using turbine") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerSuccess))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test {
            expectItem().shouldBeRight(stateCovidTrackerLoading)
            expectItem().shouldBeRight(stateCovidTrackerSuccess)
            expectComplete()
        }
    }

    should("get covid tracker by date with empty data should return loading empty success using turbine") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.right(stateCovidTrackerEmptyData))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test {
            expectItem().shouldBeRight(stateCovidTrackerLoading)
            expectItem().shouldBeRight(stateCovidTrackerEmptyData)
            expectComplete()
        }
    }

    should("get covid tracker with database problem by date should return loading and unknown database error using turbine") {
        val flow = flow {
            emit(Either.right(stateCovidTrackerLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCovidTrackerByDate(any()) } returns flow

        val flowUseCase = getCovidTracker.getCovidTrackerByDate(DATE)

        verify { repository.getCovidTrackerByDate(any()) }
        flowUseCase.test {
            expectItem().shouldBeRight(stateCovidTrackerLoading)
            expectItem().shouldBeLeft(stateErrorUnknownDatabase)
            expectComplete()
        }
    }
})