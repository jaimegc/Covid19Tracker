package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetWorldStats
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateErrorDatabaseEmpty
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListWorldStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListWorldStatsSuccess
import com.jaimegc.covid19tracker.utils.UseCaseTest
import dev.olog.flow.test.observer.test
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetWorldStatsTest : UseCaseTest() {

    private lateinit var getWorldStats: GetWorldStats

    @Before
    fun setup() {
        init()
        getWorldStats = GetWorldStats(repository)
    }

    /**********
     *  Mockk *
     **********/

    @Test
    fun `get world all stats should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.right(stateListWorldStatsSuccess))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListWorldStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListWorldStatsSuccess))
            }
        }
    }

    @Test
    fun `get world all stats should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListWorldStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorDatabaseEmpty))
            }
        }
    }

    /**************
     *  Flow Test *
     **************/

    @Test
    fun `get world all stats should return loading and success if date exists using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.right(stateListWorldStatsSuccess))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.test(this) {
            assertValues(Either.right(stateListWorldStatsLoading), Either.right(stateListWorldStatsSuccess))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get world all stats should return loading and error database empty if date doesnt exist using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListWorldStatsLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getWorldAllStats() } returns flow

        val flowUseCase = getWorldStats.getWorldAllStats()

        verify { repository.getWorldAllStats() }
        flowUseCase.test(this) {
            assertValues(Either.right(stateListWorldStatsLoading), Either.left(stateErrorDatabaseEmpty))
            assertValueCount(2)
            assertComplete()
        }
    }
}

