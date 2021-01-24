package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetRegionStats
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateRegionOneStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateRegionOneStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostConfirmedListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostDeathsListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostOpenCasesListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostRecoveredListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionOnlyStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionOnlyStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionOnlyStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListRegionStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListRegionAndStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateRegionOneStatsEmptyData
import com.jaimegc.covid19tracker.util.UseCaseTest
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
    fun `get region and all stats with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionOnlyStatsLoading))
            emit(Either.right(stateListRegionOnlyStatsEmptyData))
        }

        every { repository.getRegionAllStats(any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionAllStats(ID_COUNTRY, ID_REGION)

        verify { repository.getRegionAllStats(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionOnlyStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionOnlyStatsEmptyData))
            }
        }
    }

    @Test
    fun `get region and all stats with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionOnlyStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getRegionAllStats(any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionAllStats(ID_COUNTRY, ID_REGION)

        verify { repository.getRegionAllStats(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionOnlyStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
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
    fun `get region all stats ordered by confirmed with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionAndStatsLoading))
            emit(Either.right(stateListRegionAndStatsEmptyData))
        }

        every { repository.getRegionsAllStatsOrderByConfirmed(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAllStatsOrderByConfirmed(ID_COUNTRY)

        verify { repository.getRegionsAllStatsOrderByConfirmed(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get region all stats ordered by confirmed with database problem data should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getRegionsAllStatsOrderByConfirmed(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAllStatsOrderByConfirmed(ID_COUNTRY)

        verify { repository.getRegionsAllStatsOrderByConfirmed(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
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
    fun `get region and stats by date should with empty data return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.right(stateRegionOneStatsEmptyData))
        }

        every { repository.getRegionAndStatsByDate(any(), any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionAndStatsByDate(ID_COUNTRY, ID_REGION, DATE)

        verify { repository.getRegionAndStatsByDate(any(), any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateRegionOneStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateRegionOneStatsEmptyData))
            }
        }
    }

    @Test
    fun `get region and stats by date with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateRegionOneStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getRegionAndStatsByDate(any(), any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionAndStatsByDate(ID_COUNTRY, ID_REGION, DATE)

        verify { repository.getRegionAndStatsByDate(any(), any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateRegionOneStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
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
    fun `get regions stats order by confirmed with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.right(stateListRegionStatsEmptyData))
        }

        every { repository.getRegionsStatsOrderByConfirmed(any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsStatsOrderByConfirmed(ID_COUNTRY, DATE)

        verify { repository.getRegionsStatsOrderByConfirmed(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListRegionStatsEmptyData))
            }
        }
    }

    @Test
    fun `get regions stats order by confirmed with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListRegionStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getRegionsStatsOrderByConfirmed(any(), any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsStatsOrderByConfirmed(ID_COUNTRY, DATE)

        verify { repository.getRegionsStatsOrderByConfirmed(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListRegionStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
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
    fun `get regions and stats with most confirmed with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
        }

        every { repository.getRegionsAndStatsWithMostConfirmed(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostConfirmed(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostConfirmed(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get regions and stats with most confirmed with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getRegionsAndStatsWithMostConfirmed(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostConfirmed(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostConfirmed(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
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
    fun `get regions and stats with most deaths with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
        }

        every { repository.getRegionsAndStatsWithMostDeaths(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostDeaths(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostDeaths(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get regions and stats with most deaths with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getRegionsAndStatsWithMostDeaths(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostDeaths(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostDeaths(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
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
    fun `get regions and stats with most open cases with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
        }

        every { repository.getRegionsAndStatsWithMostOpenCases(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostOpenCases(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostOpenCases(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get regions and stats with most open cases with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getRegionsAndStatsWithMostOpenCases(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostOpenCases(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostOpenCases(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
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
    fun `get regions and stats with most recovered with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
        }

        every { repository.getRegionsAndStatsWithMostRecovered(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostRecovered(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostRecovered(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get regions and stats with most recovered with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getRegionsAndStatsWithMostRecovered(any()) } returns flow

        val flowUseCase = getRegionStats.getRegionsAndStatsWithMostRecovered(ID_COUNTRY)

        verify { repository.getRegionsAndStatsWithMostRecovered(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }
}