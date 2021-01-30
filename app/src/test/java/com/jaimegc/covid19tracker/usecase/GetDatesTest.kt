package com.jaimegc.covid19tracker.usecase

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.domain.usecase.GetDates
import com.jaimegc.covid19tracker.util.UseCaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class GetDatesTest : UseCaseTest() {

    companion object {
        private val DATES = listOf("date1", "date2", "date3")
    }

    private lateinit var getDates: GetDates

    @Before
    fun setup() {
        init()
        getDates = GetDates(repository)
    }

    @Test
    fun `get all dates should return a list with all dates`() = runBlockingTest {
        coEvery { repository.getAllDates() } returns Either.right(DATES)

        val eitherUseCase = getDates.getAllDates()

        coVerify { repository.getAllDates() }
        assertThat(eitherUseCase.isRight()).isTrue()
        eitherUseCase.map { dates ->
            assertThat(dates).hasSize(3)
            assertThat(dates).isEqualTo(DATES)
        }
    }

    @Test
    fun `get all dates with empty data should return an empty list`() = runBlockingTest {
        coEvery { repository.getAllDates() } returns Either.right(listOf())

        val eitherUseCase = getDates.getAllDates()

        coVerify { repository.getAllDates() }
        assertThat(eitherUseCase.isRight()).isTrue()
        eitherUseCase.map { dates ->
            assertThat(dates).isEmpty()
        }
    }
}

