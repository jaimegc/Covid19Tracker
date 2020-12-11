package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCountryOneStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCountryOneStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateCountryOneStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryAndStatsEmptyData
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryAndStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryAndStatsMostConfirmedSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryAndStatsMostDeathsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryAndStatsMostOpenCasesSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryAndStatsMostRecoveredSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryOnlyStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListCountryOnlyStatsSuccess
import com.jaimegc.covid19tracker.utils.UseCaseTest
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetCountryStatsTest : UseCaseTest() {

    companion object {
        private const val ID_COUNTRY = "id_country"
        private const val DATE = "date"
    }

    private lateinit var getCountryStats: GetCountryStats

    @Before
    fun setup() {
        init()
        getCountryStats = GetCountryStats(repository)
    }

    @Test
    fun `get country and all stats should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryOnlyStatsLoading))
            emit(Either.right(stateListCountryOnlyStatsSuccess))
        }

        every { repository.getCountryAllStats(any()) } returns flow

        val flowUseCase = getCountryStats.getCountryAllStats(ID_COUNTRY)

        verify { repository.getCountryAllStats(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryOnlyStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryOnlyStatsSuccess))
            }
        }
    }

    @Test
    fun `get country and all stats with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryOnlyStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountryAllStats(any()) } returns flow

        val flowUseCase = getCountryStats.getCountryAllStats(ID_COUNTRY)

        verify { repository.getCountryAllStats(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryOnlyStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get country and stats by date should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.right(stateCountryOneStatsSuccess))
        }

        every { repository.getCountryAndStatsByDate(any(), any()) } returns flow

        val flowUseCase = getCountryStats.getCountryAndStatsByDate(ID_COUNTRY, DATE)

        verify { repository.getCountryAndStatsByDate(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateCountryOneStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateCountryOneStatsSuccess))
            }
        }
    }

    @Test
    fun `get country and stats by date with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.right(stateCountryOneStatsEmptyData))
        }

        every { repository.getCountryAndStatsByDate(any(), any()) } returns flow

        val flowUseCase = getCountryStats.getCountryAndStatsByDate(ID_COUNTRY, DATE)

        verify { repository.getCountryAndStatsByDate(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateCountryOneStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateCountryOneStatsEmptyData))
            }
        }
    }

    @Test
    fun `get country and stats by date with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountryAndStatsByDate(any(), any()) } returns flow

        val flowUseCase = getCountryStats.getCountryAndStatsByDate(ID_COUNTRY, DATE)

        verify { repository.getCountryAndStatsByDate(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateCountryOneStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get countries stats order by confirmed should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsSuccess))
        }

        every { repository.getCountriesStatsOrderByConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesStatsOrderByConfirmed()

        verify { repository.getCountriesStatsOrderByConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get countries stats order by confirmed with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        every { repository.getCountriesStatsOrderByConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesStatsOrderByConfirmed()

        verify { repository.getCountriesStatsOrderByConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get countries stats order by confirmed with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountriesStatsOrderByConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesStatsOrderByConfirmed()

        verify { repository.getCountriesStatsOrderByConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get countries and stats with most confirmed should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostConfirmedSuccess))
        }

        every { repository.getCountriesAndStatsWithMostConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostConfirmed()

        verify { repository.getCountriesAndStatsWithMostConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsMostConfirmedSuccess))
            }
        }
    }

    @Test
    fun `get countries and stats with most confirmed with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        every { repository.getCountriesAndStatsWithMostConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostConfirmed()

        verify { repository.getCountriesAndStatsWithMostConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get countries and stats with most confirmed with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountriesAndStatsWithMostConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostConfirmed()

        verify { repository.getCountriesAndStatsWithMostConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get countries and stats with most deaths should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostDeathsSuccess))
        }

        every { repository.getCountriesAndStatsWithMostDeaths() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostDeaths()

        verify { repository.getCountriesAndStatsWithMostDeaths() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsMostDeathsSuccess))
            }
        }
    }

    @Test
    fun `get countries and stats with most deaths with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        every { repository.getCountriesAndStatsWithMostDeaths() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostDeaths()

        verify { repository.getCountriesAndStatsWithMostDeaths() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get countries and stats with most deaths with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountriesAndStatsWithMostDeaths() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostDeaths()

        verify { repository.getCountriesAndStatsWithMostDeaths() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get countries and stats with most open cases should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostOpenCasesSuccess))
        }

        every { repository.getCountriesAndStatsWithMostOpenCases() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostOpenCases()

        verify { repository.getCountriesAndStatsWithMostOpenCases() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsMostOpenCasesSuccess))
            }
        }
    }

    @Test
    fun `get countries and stats with most open cases with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        every { repository.getCountriesAndStatsWithMostOpenCases() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostOpenCases()

        verify { repository.getCountriesAndStatsWithMostOpenCases() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get countries and stats with most open cases with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountriesAndStatsWithMostOpenCases() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostOpenCases()

        verify { repository.getCountriesAndStatsWithMostOpenCases() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get countries and stats with most recovered should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostRecoveredSuccess))
        }

        every { repository.getCountriesAndStatsWithMostRecovered() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostRecovered()

        verify { repository.getCountriesAndStatsWithMostRecovered() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsMostRecoveredSuccess))
            }
        }
    }

    @Test
    fun `get countries and stats with most recovered with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        every { repository.getCountriesAndStatsWithMostRecovered() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostRecovered()

        verify { repository.getCountriesAndStatsWithMostRecovered() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get countries and stats with most recovered with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountriesAndStatsWithMostRecovered() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostRecovered()

        verify { repository.getCountriesAndStatsWithMostRecovered() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListCountryAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }
}