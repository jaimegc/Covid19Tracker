package com.jaimegc.covid19tracker.usecase.kotest

import app.cash.turbine.test
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.usecase.GetRegion
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionEmptySuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionSuccess
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import dev.olog.flow.test.observer.test
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow

class GetRegionKotestTest : ShouldSpec({

    val ID_COUNTRY = "id_country"

    lateinit var getRegion: GetRegion
    val repository: CovidTrackerRepository = mockk()

    beforeTest {
        MockKAnnotations.init(this)
        getRegion = GetRegion(repository)
    }

    /**********
     *  Mockk *
     **********/

    should("get regions by country should return loading and success") {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionSuccess))
        }

        every { repository.getRegionsByCountry(any()) } returns flow

        val flowUseCase = getRegion.getRegionsByCountry(ID_COUNTRY)

        verify { repository.getRegionsByCountry(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListRegionLoading)
                1 -> data.shouldBeRight(stateListRegionSuccess)
            }
        }
    }

    should("get regions by country with empty data should return loading and success") {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionEmptySuccess))
        }

        every { repository.getRegionsByCountry(any()) } returns flow

        val flowUseCase = getRegion.getRegionsByCountry(ID_COUNTRY)

        verify { repository.getRegionsByCountry(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListRegionLoading)
                1 -> data.shouldBeRight(stateListRegionEmptySuccess)
            }
        }
    }

    /**************
     *  Flow Test *
     **************/

    should("get regions by country should return loading and success using flow test") {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionSuccess))
        }

        every { repository.getRegionsByCountry(any()) } returns flow

        val flowUseCase = getRegion.getRegionsByCountry(ID_COUNTRY)

        verify { repository.getRegionsByCountry(any()) }
        flowUseCase.test(this) {
            valueAt(0).shouldBeRight(stateListRegionLoading)
            valueAt(1).shouldBeRight(stateListRegionSuccess)
            assertValueCount(2)
            assertComplete()
        }
    }

    should("get regions by country with empty data should return loading and success using flow test") {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionEmptySuccess))
        }

        every { repository.getRegionsByCountry(any()) } returns flow

        val flowUseCase = getRegion.getRegionsByCountry(ID_COUNTRY)

        verify { repository.getRegionsByCountry(any()) }
        flowUseCase.test(this) {
            valueAt(0).shouldBeRight(stateListRegionLoading)
            valueAt(1).shouldBeRight(stateListRegionEmptySuccess)
            assertValueCount(2)
            assertComplete()
        }
    }

    /************
     *  Turbine *
     ************/

    should("get regions by country should return loading and success using turbine") {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionSuccess))
        }

        every { repository.getRegionsByCountry(any()) } returns flow

        val flowUseCase = getRegion.getRegionsByCountry(ID_COUNTRY)

        verify { repository.getRegionsByCountry(any()) }
        flowUseCase.test {
            expectItem().shouldBeRight(stateListRegionLoading)
            expectItem().shouldBeRight(stateListRegionSuccess)
            expectComplete()
        }
    }

    should("get regions by country with empty data should return loading and success using turbine") {
        val flow = flow {
            emit(Either.right(stateListRegionLoading))
            emit(Either.right(stateListRegionEmptySuccess))
        }

        every { repository.getRegionsByCountry(any()) } returns flow

        val flowUseCase = getRegion.getRegionsByCountry(ID_COUNTRY)

        verify { repository.getRegionsByCountry(any()) }
        flowUseCase.test {
            expectItem().shouldBeRight(stateListRegionLoading)
            expectItem().shouldBeRight(stateListRegionEmptySuccess)
            expectComplete()
        }
    }
})

