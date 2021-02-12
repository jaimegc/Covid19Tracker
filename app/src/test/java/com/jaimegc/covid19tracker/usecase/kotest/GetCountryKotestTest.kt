package com.jaimegc.covid19tracker.usecase.kotest

import app.cash.turbine.test
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountrySuccess
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

class GetCountryKotestTest : ShouldSpec({

    lateinit var getCountry: GetCountry
    val repository: CovidTrackerRepository = mockk()

    beforeTest {
        MockKAnnotations.init(this)
        getCountry = GetCountry(repository)
    }

    /**********
     *  Mockk *
     **********/

    should("get countries should return loading and success") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountrySuccess))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryLoading)
                1 -> data.shouldBeRight(stateListCountrySuccess)
            }
        }
    }

    should("get countries with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountryEmptyData))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryLoading)
                1 -> data.shouldBeRight(stateListCountryEmptyData)
            }
        }
    }

    should("get countries with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    /**************
     *  Flow Test *
     **************/

    should("get countries should return loading and success using flow test") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountrySuccess))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.test(this) {
            assertValues(Either.right(stateListCountryLoading), Either.right(stateListCountrySuccess))
            valueAt(0).shouldBeRight(stateListCountryLoading)
            valueAt(1).shouldBeRight(stateListCountrySuccess)
            assertValueCount(2)
            assertComplete()
        }
    }

    should("get countries with empty data should return loading and empty success using flow test") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountryEmptyData))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.test(this) {
            valueAt(0).shouldBeRight(stateListCountryLoading)
            valueAt(1).shouldBeRight(stateListCountryEmptyData)
            assertValueCount(2)
            assertComplete()
        }
    }

    should("get countries with database problem should return loading and unknown database error using flow test") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.test(this) {
            valueAt(0).shouldBeRight(stateListCountryLoading)
            valueAt(1).shouldBeLeft(stateErrorUnknownDatabase)
            assertValueCount(2)
            assertComplete()
        }
    }

    /************
     *  Turbine *
     ************/

    should("get countries should return loading and success using turbine") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountrySuccess))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.test {
            expectItem().shouldBeRight(stateListCountryLoading)
            expectItem().shouldBeRight(stateListCountrySuccess)
            expectComplete()
        }
    }

    should("get countries with empty data should return loading and empty success using turbine") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountryEmptyData))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.test {
            expectItem().shouldBeRight(stateListCountryLoading)
            expectItem().shouldBeRight(stateListCountryEmptyData)
            expectComplete()
        }
    }

    should("get countries with database problem should return loading and unknown database error using turbine") {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.test {
            expectItem().shouldBeRight(stateListCountryLoading)
            expectItem().shouldBeLeft(stateErrorUnknownDatabase)
            expectComplete()
        }
    }
})

