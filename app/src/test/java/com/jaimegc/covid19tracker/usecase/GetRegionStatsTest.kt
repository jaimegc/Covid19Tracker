package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetRegionStats
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateRegionOneStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateRegionOneStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateErrorDatabaseEmpty
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateLineChartMostConfirmedListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateLineChartMostDeathsListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateLineChartMostOpenCasesListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateLineChartMostRecoveredListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionAndStatsEmptySuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionAndStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionOnlyStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionOnlyStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionStatsLoading
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateListRegionStatsSuccess
import com.jaimegc.covid19tracker.utils.ScreenStateBuilder.stateMenuItemViewTypeListRegionAndStatsLoading
import com.jaimegc.covid19tracker.utils.UseCaseTest
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetRegionStatsTest : UseCaseTest() {

    companion object {
        private const val ID_COUNTRY = "id_country"
        private const val ID_REGION = "id_region"
        private const val DATE = "date"
    }

    private lateinit var getRegionStats: GetRegionStats

    @Before
    fun setup() {
        init()
        getRegionStats = GetRegionStats(repository)
    }

    @Test
    fun `get region and all stats should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionOnlyStatsLoading))
            emit(Either.right(stateListRegionOnlyStatsSuccess))
        }

        every { repository.getRegionAllStats(any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionAllStats(ID_COUNTRY, ID_REGION)

        verify { repository.getRegionAllStats(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionOnlyStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionOnlyStatsSuccess))
            }
        }
    }

    @Test
    fun `get region and all stats should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionOnlyStatsLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getRegionAllStats(any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionAllStats(ID_COUNTRY, ID_REGION)

        verify { repository.getRegionAllStats(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionOnlyStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorDatabaseEmpty))
            }
        }
    }

    @Test
    fun `get region all stats ordered by confirmed should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionAndStatsLoading))
            emit(Either.right(stateListRegionAndStatsSuccess))
        }

        every { repository.getRegionsAllStatsOrderByConfirmed(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAllStatsOrderByConfirmed(ID_COUNTRY)

        verify { repository.getRegionsAllStatsOrderByConfirmed(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get region all stats ordered by confirmed with empty data should return loading and success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionAndStatsLoading))
            emit(Either.right(stateListRegionAndStatsEmptySuccess))
        }

        every { repository.getRegionsAllStatsOrderByConfirmed(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAllStatsOrderByConfirmed(ID_COUNTRY)

        verify { repository.getRegionsAllStatsOrderByConfirmed(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionAndStatsEmptySuccess))
            }
        }
    }

    @Test
    fun `get region and stats by date should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.right(stateRegionOneStatsSuccess))
        }

        every { repository.getRegionAndStatsByDate(any(), any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionAndStatsByDate(ID_COUNTRY, ID_REGION, DATE)

        verify { repository.getRegionAndStatsByDate(any(), any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateRegionOneStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateRegionOneStatsSuccess))
            }
        }
    }

    @Test
    fun `get region and stats by date should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getRegionAndStatsByDate(any(), any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionAndStatsByDate(ID_COUNTRY, ID_REGION, DATE)

        verify { repository.getRegionAndStatsByDate(any(), any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateRegionOneStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorDatabaseEmpty))
            }
        }
    }

    @Test
    fun `get regions stats order by confirmed should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsSuccess))
        }

        every { repository.getRegionsStatsOrderByConfirmed(any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsStatsOrderByConfirmed(ID_COUNTRY, DATE)

        verify { repository.getRegionsStatsOrderByConfirmed(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionStatsSuccess))
            }
        }
    }

    @Test
    fun `get regions stats order by confirmed should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getRegionsStatsOrderByConfirmed(any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsStatsOrderByConfirmed(ID_COUNTRY, DATE)

        verify { repository.getRegionsStatsOrderByConfirmed(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorDatabaseEmpty))
            }
        }
    }

    @Test
    fun `get regions and stats with most confirmed should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostConfirmedListRegionAndStatsSuccess))
        }

        every { repository.getRegionsAndStatsWithMostConfirmed(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostConfirmed(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostConfirmed(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostConfirmedListRegionAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get regions and stats with most confirmed should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getRegionsAndStatsWithMostConfirmed(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostConfirmed(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostConfirmed(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorDatabaseEmpty))
            }
        }
    }

    @Test
    fun `get regions and stats with most deaths should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostDeathsListRegionAndStatsSuccess))
        }

        every { repository.getRegionsAndStatsWithMostDeaths(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostDeaths(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostDeaths(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostDeathsListRegionAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get regions and stats with most deaths should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getRegionsAndStatsWithMostDeaths(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostDeaths(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostDeaths(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorDatabaseEmpty))
            }
        }
    }

    @Test
    fun `get regions and stats with most open cases should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostOpenCasesListRegionAndStatsSuccess))
        }

        every { repository.getRegionsAndStatsWithMostOpenCases(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostOpenCases(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostOpenCases(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostOpenCasesListRegionAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get regions and stats with most open cases should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getRegionsAndStatsWithMostOpenCases(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostOpenCases(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostOpenCases(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorDatabaseEmpty))
            }
        }
    }

    @Test
    fun `get regions and stats with most recovered should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostRecoveredListRegionAndStatsSuccess))
        }

        every { repository.getRegionsAndStatsWithMostRecovered(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostRecovered(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostRecovered(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostRecoveredListRegionAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get regions and stats with most recovered should return loading and error database empty if date doesnt exist`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorDatabaseEmpty))
        }

        every { repository.getRegionsAndStatsWithMostRecovered(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostRecovered(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostRecovered(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorDatabaseEmpty))
            }
        }
    }
}