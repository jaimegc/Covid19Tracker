package com.jaimegc.covid19tracker.usecase.kotest

import arrow.core.Either
import com.jaimegc.covid19tracker.data.repository.CovidTrackerRepository
import com.jaimegc.covid19tracker.domain.usecase.GetDates
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class GetDatesKotestTest : ShouldSpec({

    val DATES = listOf("date1", "date2", "date3")

    val repository: CovidTrackerRepository = mockk()
    lateinit var getDates: GetDates

    beforeTest {
        MockKAnnotations.init(this)
        getDates = GetDates(repository)
    }

    should("get all dates should return a list with all dates") {
        coEvery { repository.getAllDates() } returns Either.right(DATES)

        val eitherUseCase = getDates.getAllDates()

        coVerify { repository.getAllDates() }
        eitherUseCase.shouldBeRight(DATES)
    }

    should("get all dates with empty data should return an empty list") {
        coEvery { repository.getAllDates() } returns Either.right(listOf())

        val eitherUseCase = getDates.getAllDates()

        coVerify { repository.getAllDates() }
        eitherUseCase.shouldBeRight(emptyList())
    }
})

