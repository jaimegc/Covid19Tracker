package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateErrorDatabaseEmpty
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountrySuccess
import com.jaimegc.covid19tracker.utils.UseCaseTest
import dev.olog.flow.test.observer.test
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetCountryTest : UseCaseTest() {

    private lateinit var getCountry: GetCountry

    @Before
    fun setup() {
        init()
        getCountry = GetCountry(repository)
    }

    /**********
     *  Mockk *
     **********/

    @Test
    fun `get countries should return loading and success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountrySuccess))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountrySuccess))
            }
        }
    }

    @Test
    fun `get countries should return loading and error if database is empty`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorDatabaseEmpty))
            }
        }
    }

    /**************
     *  Flow Test *
     **************/

    @Test
    fun `get countries should return loading and success using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountrySuccess))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.test(this) {
            assertValues(Either.right(stateListCountryLoading), Either.right(stateListCountrySuccess))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get countries should return loading and error if database is empty using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.test(this) {
            assertValues(Either.right(stateListCountryLoading), Either.left(stateErrorDatabaseEmpty))
            assertValueCount(2)
            assertComplete()
        }
    }
}

