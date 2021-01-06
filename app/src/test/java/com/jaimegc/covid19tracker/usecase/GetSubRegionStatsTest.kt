package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetSubRegionStats
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostConfirmedListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostDeathsListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostOpenCasesListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateLineChartMostRecoveredListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListSubRegionStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListSubRegionAndStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateMenuItemViewTypeListSubRegionAndStatsLoading
import com.jaimegc.covid19tracker.utils.UseCaseTest
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetSubRegionStatsTest : UseCaseTest() {

    companion object {
        private const val ID_COUNTRY = "id_country"
        private const val ID_REGION = "id_region"
        private const val DATE = "date"
    }

    private lateinit var getSubRegionStats: GetSubRegionStats

    @Before
    fun setup() {
        init()
        getSubRegionStats = GetSubRegionStats(repository)
    }

    @Test
    fun `get subregion all stats ordered by confirmed should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListSubRegionAndStatsLoading))
            emit(Either.right(stateListSubRegionAndStatsSuccess))
        }

        every { repository.getSubRegionsAllStatsOrderByConfirmed(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAllStatsOrderByConfirmed(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAllStatsOrderByConfirmed(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get subregion all stats ordered by confirmed with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListSubRegionAndStatsLoading))
            emit(Either.right(stateListSubRegionAndStatsEmptyData))
        }

        every { repository.getSubRegionsAllStatsOrderByConfirmed(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAllStatsOrderByConfirmed(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAllStatsOrderByConfirmed(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get subregion all stats ordered by confirmed with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListSubRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getSubRegionsAllStatsOrderByConfirmed(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAllStatsOrderByConfirmed(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAllStatsOrderByConfirmed(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get subregions stats order by confirmed should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.right(stateListSubRegionStatsSuccess))
        }

        every { repository.getSubRegionsStatsOrderByConfirmed(any(), any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsStatsOrderByConfirmed(ID_COUNTRY, ID_REGION, DATE)

        verify { repository.getSubRegionsStatsOrderByConfirmed(any(), any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionStatsSuccess))
            }
        }
    }

    @Test
    fun `get subregions stats order by confirmed with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.right(stateListSubRegionStatsEmptyData))
        }

        every { repository.getSubRegionsStatsOrderByConfirmed(any(), any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsStatsOrderByConfirmed(ID_COUNTRY, ID_REGION, DATE)

        verify { repository.getSubRegionsStatsOrderByConfirmed(any(), any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionStatsEmptyData))
            }
        }
    }

    @Test
    fun `get subregions stats order by confirmed with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateListSubRegionStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getSubRegionsStatsOrderByConfirmed(any(), any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsStatsOrderByConfirmed(ID_COUNTRY, ID_REGION, DATE)

        verify { repository.getSubRegionsStatsOrderByConfirmed(any(), any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateListSubRegionStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get subregions and stats with most confirmed should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsSuccess))
        }

        every { repository.getSubRegionsAndStatsWithMostConfirmed(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostConfirmed(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostConfirmedListSubRegionAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get subregions and stats with most confirmed with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
        }

        every { repository.getSubRegionsAndStatsWithMostConfirmed(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostConfirmed(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get subregions and stats with most confirmed with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getSubRegionsAndStatsWithMostConfirmed(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostConfirmed(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostConfirmed(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get subregions and stats with most deaths should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostDeathsListSubRegionAndStatsSuccess))
        }

        every { repository.getSubRegionsAndStatsWithMostDeaths(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostDeaths(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostDeathsListSubRegionAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get subregions and stats with most deaths with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
        }

        every { repository.getSubRegionsAndStatsWithMostDeaths(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostDeaths(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get subregions and stats with most deaths with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getSubRegionsAndStatsWithMostDeaths(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostDeaths(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostDeaths(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get subregions and stats with most open cases should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostOpenCasesListSubRegionAndStatsSuccess))
        }

        every { repository.getSubRegionsAndStatsWithMostOpenCases(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostOpenCases(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostOpenCasesListSubRegionAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get subregions and stats with most open cases with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
        }

        every { repository.getSubRegionsAndStatsWithMostOpenCases(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostOpenCases(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get subregions and stats with most open cases with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getSubRegionsAndStatsWithMostOpenCases(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostOpenCases(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostOpenCases(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }

    @Test
    fun `get subregions and stats with most recovered should return loading and success if date exists`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateLineChartMostRecoveredListSubRegionAndStatsSuccess))
        }

        every { repository.getSubRegionsAndStatsWithMostRecovered(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostRecovered(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateLineChartMostRecoveredListSubRegionAndStatsSuccess))
            }
        }
    }

    @Test
    fun `get subregions and stats with most recovered with empty data should return loading and empty success`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
        }

        every { repository.getSubRegionsAndStatsWithMostRecovered(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostRecovered(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsEmptyData))
            }
        }
    }

    @Test
    fun `get subregions and stats with most recovered with database problem should return loading and unknown database error`() = runBlockingTest {
        val flow = flow {
            emit(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getSubRegionsAndStatsWithMostRecovered(any(), any()) } returns flow

        val flowUseCase = getSubRegionStats.getSubRegionsAndStatsWithMostRecovered(ID_COUNTRY, ID_REGION)

        verify { repository.getSubRegionsAndStatsWithMostRecovered(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> assertThat(data).isEqualTo(Either.right(stateMenuItemViewTypeListSubRegionAndStatsLoading))
                1 -> assertThat(data).isEqualTo(Either.left(stateErrorUnknownDatabase))
            }
        }
    }
}