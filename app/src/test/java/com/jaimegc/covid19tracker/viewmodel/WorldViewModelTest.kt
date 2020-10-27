package com.jaimegc.covid19tracker.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import arrow.core.Either
import com.jaimegc.covid19tracker.domain.model.CovidTracker
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.states.State
import com.jaimegc.covid19tracker.domain.states.StateError
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.domain.usecase.GetWorldAndCountries
import com.jaimegc.covid19tracker.domain.usecase.GetWorldStats
import com.jaimegc.covid19tracker.extension.getOrAwaitValue
import com.jaimegc.covid19tracker.extension.observeForTesting
import com.jaimegc.covid19tracker.ui.base.states.ScreenState
import com.jaimegc.covid19tracker.ui.base.states.WorldStateScreen
import com.jaimegc.covid19tracker.ui.model.toUI
import com.jaimegc.covid19tracker.ui.world.WorldViewModel
import com.jaimegc.covid19tracker.utils.MainCoroutineRule
import com.jaimegc.covid19tracker.utils.ModelBuilder.covidTracker
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class WorldViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineRule()

    private val captor = argumentCaptor<ScreenState<WorldStateScreen>>()

    private lateinit var worldViewModel: WorldViewModel
    private val getWorldAndCountries: GetWorldAndCountries = mock()
    private val getWorldStats: GetWorldStats = mock()
    private val getCountryStats: GetCountryStats = mock()

    private val stateObserver: Observer<ScreenState<WorldStateScreen>> = mock()
    private val stateLoading: State<CovidTracker> = State.Loading()
    private val stateErrorDatabaseEmpty: StateError<DomainError> =
        StateError.Error(DomainError.DatabaseEmptyData)
    private val stateScreenErrorDatabaseEmpty: ScreenState.Error<WorldStateScreen.SomeError> =
        ScreenState.Error(
            WorldStateScreen.SomeError((stateErrorDatabaseEmpty as StateError.Error).error.toUI())
        )
    private val stateSuccess: State<CovidTracker> = State.Success(covidTracker)
    private val stateScreenSuccessCovidTracker =
        ScreenState.Render(
            WorldStateScreen.SuccessCovidTracker((stateSuccess as State.Success).data.toUI())
        )

    @Before
    fun setup() {
        worldViewModel = WorldViewModel(getWorldAndCountries, getWorldStats, getCountryStats)

        worldViewModel.screenState.observeForever(stateObserver)
    }

    @Test
    fun `get list chart stats should return loading and and success if date exists`() {
        val flow = flow {
            emit(Either.right(stateLoading))
            emit(Either.right(stateSuccess))
        }

        whenever(getWorldAndCountries.getWorldAndCountriesByDate()).thenReturn(flow)

        worldViewModel.getListChartStats()

        val liveData = worldViewModel.screenState
        liveData.observeForever(stateObserver)

        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())
        assertEquals(ScreenState.Loading, captor.firstValue)
        assertEquals(stateScreenSuccessCovidTracker::class, captor.secondValue::class)
    }

    @Test
    fun `get list chart stats should return loading and error database empty if date doesnt exist`() {
        val flow = flow {
            emit(Either.right(stateLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        whenever(getWorldAndCountries.getWorldAndCountriesByDate()).thenReturn(flow)

        worldViewModel.getListChartStats()

        val liveData = worldViewModel.screenState
        liveData.observeForever(stateObserver)

        verify(stateObserver, Mockito.times(2)).onChanged(captor.capture())
        assertEquals(ScreenState.Loading, captor.firstValue)
        assertEquals(stateScreenErrorDatabaseEmpty::class, captor.secondValue::class)
    }

    /*****************************************************************
     *  getOrAwaitValue and observeForTesting from Google repository *
     *****************************************************************/

    @Test
    fun `get list chart stats should return loading and error database empty if date doesnt exist using getOrAwaitValue & observeForTesting`() {
        val flow = flow {
            emit(Either.right(stateLoading))
            delay(10)
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        whenever(getWorldAndCountries.getWorldAndCountriesByDate()).thenReturn(flow)

        worldViewModel.getListChartStats()

        worldViewModel.screenState.getOrAwaitValue {
            val loading = worldViewModel.screenState.value
            assertEquals(ScreenState.Loading, loading)
            coroutineScope.advanceUntilIdle()
            val error = worldViewModel.screenState.value
            assertNotNull(error)
            assertEquals(stateScreenErrorDatabaseEmpty::class, error!!::class)
        }

        worldViewModel.getListChartStats()

        worldViewModel.screenState.observeForTesting {
            val loading = worldViewModel.screenState.value
            assertEquals(ScreenState.Loading, loading)
            coroutineScope.advanceUntilIdle()
            val error = worldViewModel.screenState.value
            assertNotNull(error)
            assertEquals(stateScreenErrorDatabaseEmpty::class, error!!::class)
        }
    }
}