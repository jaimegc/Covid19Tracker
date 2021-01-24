package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetRegion
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionSuccess
import com.jaimegc.covid19tracker.util.UseCaseTest
import dev.olog.flow.test.observer.test
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetRegionTest : UseCaseTest() {

    companion object {
        private const val ID_COUNTRY = "id_country"
    }

    private lateinit var getRegion: GetRegion

    @Before
    fun setup() {
        init()
        getRegion = GetRegion(repository)
    }

    /**********
     *  Mockk *
     **********/

    @Test
    fun `get regions by country should return loading and success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionSuccess))
        }

        every { repository.getRegionsByCountry(any()) } returns flow

        val flowUseCase = getRegion.getRegionsByCountry(ID_COUNTRY)

        verify { repository.getRegionsByCountry(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionSuccess))
            }
        }
    }

    @Test
    fun `get regions by country with empty data should return loading and success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionEmptySuccess))
        }

        every { repository.getRegionsByCountry(any()) } returns flow

        val flowUseCase = getRegion.getRegionsByCountry(ID_COUNTRY)

        verify { repository.getRegionsByCountry(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionEmptySuccess))
            }
        }
    }

    /**************
     *  Flow Test *
     **************/

    @Test
    fun `get regions by country should return loading and success using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionSuccess))
        }

        every { repository.getRegionsByCountry(any()) } returns flow

        val flowUseCase = getRegion.getRegionsByCountry(ID_COUNTRY)

        verify { repository.getRegionsByCountry(any()) }
        flowUseCase.test(this) {
            assertValues(Either.right(stateListRegionLoading), Either.right(stateListRegionSuccess))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get regions by country with empty data should return loading and success using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionEmptySuccess))
        }

        every { repository.getRegionsByCountry(any()) } returns flow

        val flowUseCase = getRegion.getRegionsByCountry(ID_COUNTRY)

        verify { repository.getRegionsByCountry(any()) }
        flowUseCase.test(this) {
            assertValues(Either.right(stateListRegionLoading), Either.right(stateListRegionEmptySuccess))
            assertValueCount(2)
            assertComplete()
        }
    }
}

