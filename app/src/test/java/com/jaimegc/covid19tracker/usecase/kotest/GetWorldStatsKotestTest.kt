package com.jaimegc.covid19tracker.usecase.kotest

import app.cash.turbine.test
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.usecase.GetWorldStats
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListWorldStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListWorldStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListWorldStatsSuccess
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

class GetWorldStatsKotestTest : ShouldSpec({

    val repository: CovidTrackerRepository = mockk()
    lateinit var getWorldStats: GetWorldStats

    beforeTest {
        MockKAnnotations.init(this)
        getWorldStats = GetWorldStats(repository)
    }

    /**********
     *  Mockk *
     **********/

    should("get world all stats should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.right(stateListWorldStatsSuccess))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListWorldStatsLoading)
                1 -> data.shouldBeRight(stateListWorldStatsSuccess)
            }
        }
    }

    should("get world all stats with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListWorldStatsLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    should("get world all stats should with database problem return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListWorldStatsLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    /**************
     *  Flow Test *
     **************/

    should("get world all stats should return loading and success if date exists using flow test") {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.right(stateListWorldStatsSuccess))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.test(this) {
            valueAt(0).shouldBeRight(stateListWorldStatsLoading)
            valueAt(1).shouldBeRight(stateListWorldStatsSuccess)
            assertValueCount(2)
            assertComplete()
        }
    }

    should("get world all stats with empty data should return loading and empty success using flow test") {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.right(stateListWorldStatsEmptyData))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.test(this) {
            valueAt(0).shouldBeRight(stateListWorldStatsLoading)
            valueAt(1).shouldBeRight(stateListWorldStatsEmptyData)
            assertValueCount(2)
            assertComplete()
        }
    }

    should("get world all stats should with database problem return loading and unknown database error using flow test") {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.test(this) {
            valueAt(0).shouldBeRight(stateListWorldStatsLoading)
            valueAt(1).shouldBeLeft(stateErrorUnknownDatabase)
            assertValueCount(2)
            assertComplete()
        }
    }

    /************
     *  Turbine *
     ************/

    should("get world all stats should return loading and success if date exists using turbine") {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.right(stateListWorldStatsSuccess))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.test {
            expectItem().shouldBeRight(stateListWorldStatsLoading)
            expectItem().shouldBeRight(stateListWorldStatsSuccess)
            expectComplete()
        }
    }

    should("get world all stats with empty data should return loading and empty success using turbine") {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.right(stateListWorldStatsEmptyData))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.test {
            expectItem().shouldBeRight(stateListWorldStatsLoading)
            expectItem().shouldBeRight(stateListWorldStatsEmptyData)
            expectComplete()
        }
    }

    should("get world all stats should with database problem return loading and unknown database error using turbine") {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.test {
            expectItem().shouldBeRight(stateListWorldStatsLoading)
            expectItem().shouldBeLeft(stateErrorUnknownDatabase)
            expectComplete()
        }
    }
})

