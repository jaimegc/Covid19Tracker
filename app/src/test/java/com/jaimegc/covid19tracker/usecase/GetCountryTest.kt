package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetCountry
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryEmptyData
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
    fun `get countries with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountryEmptyData))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryEmptyData))
            }
        }
    }

    @Test
    fun `get countries with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
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
    fun `get countries with empty data should return loading and empty success using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.right(stateListCountryEmptyData))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.test(this) {
            assertValues(Either.right(stateListCountryLoading), Either.right(stateListCountryEmptyData))
            assertValueCount(2)
            assertComplete()
        }
    }

    @Test
    fun `get countries with database problem should return loading and unknown database error using flow test`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountries() } returns flow

        val flowUseCase = getCountry.getCountries()

        verify { repository.getCountries() }
        flowUseCase.test(this) {
            assertValues(Either.right(stateListCountryLoading), Either.left(stateErrorUnknownDatabase))
            assertValueCount(2)
            assertComplete()
        }
    }
}

