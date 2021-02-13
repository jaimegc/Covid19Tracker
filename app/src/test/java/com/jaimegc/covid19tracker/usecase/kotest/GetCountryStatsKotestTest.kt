package com.jaimegc.covid19tracker.usecase.kotest

import arrow.core.Either
import com.jaimegc.covid19tracker.domain.usecase.GetCountryStats
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCountryOneStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCountryOneStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateCountryOneStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateErrorUnknownDatabase
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsEmptyData
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostConfirmedSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostDeathsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostOpenCasesSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsMostRecoveredSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryAndStatsSuccess
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryOnlyStatsLoading
import com.jaimegc.covid19tracker.ScreenStateFactoryTest.stateListCountryOnlyStatsSuccess
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow

class GetCountryStatsKotestTest : ShouldSpec({

    val ID_COUNTRY = "id_country"
    val DATE = "date"

    lateinit var getCountryStats: GetCountryStats
    val repository: CovidTrackerRepository = mockk()

    beforeTest {
        MockKAnnotations.init(this)
        getCountryStats = GetCountryStats(repository)
    }

    should("get country and all stats should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateListCountryOnlyStatsLoading))
            emit(Either.right(stateListCountryOnlyStatsSuccess))
        }

        every { repository.getCountryAllStats(any()) } returns flow

        val flowUseCase = getCountryStats.getCountryAllStats(ID_COUNTRY)

        verify { repository.getCountryAllStats(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryOnlyStatsLoading)
                1 -> data.shouldBeRight(stateListCountryOnlyStatsSuccess)
            }
        }
    }

    should("get country and all stats with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateListCountryOnlyStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountryAllStats(any()) } returns flow

        val flowUseCase = getCountryStats.getCountryAllStats(ID_COUNTRY)

        verify { repository.getCountryAllStats(any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryOnlyStatsLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    should("get country and stats by date should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.right(stateCountryOneStatsSuccess))
        }

        every { repository.getCountryAndStatsByDate(any(), any()) } returns flow

        val flowUseCase = getCountryStats.getCountryAndStatsByDate(ID_COUNTRY, DATE)

        verify { repository.getCountryAndStatsByDate(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateCountryOneStatsLoading)
                1 -> data.shouldBeRight(stateCountryOneStatsSuccess)
            }
        }
    }

    should("get country and stats by date with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.right(stateCountryOneStatsEmptyData))
        }

        every { repository.getCountryAndStatsByDate(any(), any()) } returns flow

        val flowUseCase = getCountryStats.getCountryAndStatsByDate(ID_COUNTRY, DATE)

        verify { repository.getCountryAndStatsByDate(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateCountryOneStatsLoading)
                1 -> data.shouldBeRight(stateCountryOneStatsEmptyData)
            }
        }
    }

    should("get country and stats by date with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateCountryOneStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountryAndStatsByDate(any(), any()) } returns flow

        val flowUseCase = getCountryStats.getCountryAndStatsByDate(ID_COUNTRY, DATE)

        verify { repository.getCountryAndStatsByDate(any(), any()) }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateCountryOneStatsLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    should("get countries stats order by confirmed should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsSuccess))
        }

        every { repository.getCountriesStatsOrderByConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesStatsOrderByConfirmed()

        verify { repository.getCountriesStatsOrderByConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeRight(stateListCountryAndStatsSuccess)
            }
        }
    }

    should("get countries stats order by confirmed with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        every { repository.getCountriesStatsOrderByConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesStatsOrderByConfirmed()

        verify { repository.getCountriesStatsOrderByConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeRight(stateListCountryAndStatsEmptyData)
            }
        }
    }

    should("get countries stats order by confirmed with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountriesStatsOrderByConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesStatsOrderByConfirmed()

        verify { repository.getCountriesStatsOrderByConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    should("get countries and stats with most confirmed should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostConfirmedSuccess))
        }

        every { repository.getCountriesAndStatsWithMostConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostConfirmed()

        verify { repository.getCountriesAndStatsWithMostConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeRight(stateListCountryAndStatsMostConfirmedSuccess)
            }
        }
    }

    should("get countries and stats with most confirmed with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        every { repository.getCountriesAndStatsWithMostConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostConfirmed()

        verify { repository.getCountriesAndStatsWithMostConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeRight(stateListCountryAndStatsEmptyData)
            }
        }
    }

    should("get countries and stats with most confirmed with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountriesAndStatsWithMostConfirmed() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostConfirmed()

        verify { repository.getCountriesAndStatsWithMostConfirmed() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    should("get countries and stats with most deaths should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostDeathsSuccess))
        }

        every { repository.getCountriesAndStatsWithMostDeaths() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostDeaths()

        verify { repository.getCountriesAndStatsWithMostDeaths() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeRight(stateListCountryAndStatsMostDeathsSuccess)
            }
        }
    }

    should("get countries and stats with most deaths with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        every { repository.getCountriesAndStatsWithMostDeaths() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostDeaths()

        verify { repository.getCountriesAndStatsWithMostDeaths() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeRight(stateListCountryAndStatsEmptyData)
            }
        }
    }

    should("get countries and stats with most deaths with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountriesAndStatsWithMostDeaths() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostDeaths()

        verify { repository.getCountriesAndStatsWithMostDeaths() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    should("get countries and stats with most open cases should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostOpenCasesSuccess))
        }

        every { repository.getCountriesAndStatsWithMostOpenCases() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostOpenCases()

        verify { repository.getCountriesAndStatsWithMostOpenCases() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeRight(stateListCountryAndStatsMostOpenCasesSuccess)
            }
        }
    }

    should("get countries and stats with most open cases with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        every { repository.getCountriesAndStatsWithMostOpenCases() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostOpenCases()

        verify { repository.getCountriesAndStatsWithMostOpenCases() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeRight(stateListCountryAndStatsEmptyData)
            }
        }
    }

    should("get countries and stats with most open cases with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountriesAndStatsWithMostOpenCases() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostOpenCases()

        verify { repository.getCountriesAndStatsWithMostOpenCases() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }

    should("get countries and stats with most recovered should return loading and success if date exists") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsMostRecoveredSuccess))
        }

        every { repository.getCountriesAndStatsWithMostRecovered() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostRecovered()

        verify { repository.getCountriesAndStatsWithMostRecovered() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeRight(stateListCountryAndStatsMostRecoveredSuccess)
            }
        }
    }

    should("get countries and stats with most recovered with empty data should return loading and empty success") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.right(stateListCountryAndStatsEmptyData))
        }

        every { repository.getCountriesAndStatsWithMostRecovered() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostRecovered()

        verify { repository.getCountriesAndStatsWithMostRecovered() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeRight(stateListCountryAndStatsEmptyData)
            }
        }
    }

    should("get countries and stats with most recovered with database problem should return loading and unknown database error") {
        val flow = flow {
            emit(Either.right(stateListCountryAndStatsLoading))
            emit(Either.left(stateErrorUnknownDatabase))
        }

        every { repository.getCountriesAndStatsWithMostRecovered() } returns flow

        val flowUseCase = getCountryStats.getCountriesAndStatsWithMostRecovered()

        verify { repository.getCountriesAndStatsWithMostRecovered() }
        flowUseCase.collectIndexed { index, data ->
            when (index) {
                0 -> data.shouldBeRight(stateListCountryAndStatsLoading)
                1 -> data.shouldBeLeft(stateErrorUnknownDatabase)
            }
        }
    }
})