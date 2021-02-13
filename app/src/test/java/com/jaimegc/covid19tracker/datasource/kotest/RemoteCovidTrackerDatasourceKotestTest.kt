package com.jaimegc.covid19tracker.datasource.kotest

import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.DATE
import com.jaimegc.covid19tracker.ModelFactoryTest.covidTrackerDto
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.squareup.moshi.JsonDataException
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

class RemoteCovidTrackerDatasourceKotestTest : ShouldSpec({

    val apiClient: CovidTrackerApiClient = mockk(relaxed = true)
    val preferences: CovidTrackerPreferences = mockk(relaxed = true)
    val responseBody: ResponseBody = mockk(relaxed = true)
    lateinit var remote: RemoteCovidTrackerDatasource

    beforeTest {
        MockKAnnotations.init(this)
        remote = RemoteCovidTrackerDatasource(apiClient, preferences)
    }

    should("get covid tracker by date should return covid tracker if date exists") {
        coEvery { apiClient.getCovidTrackerByDate(any()) } returns covidTrackerDto

        val data = remote.getCovidTrackerByDate(DATE)

        coVerify { apiClient.getCovidTrackerByDate(any()) }
        verify { preferences.saveTime() }
        data.shouldBeRight(covidTrackerDto.toDomain(DATE))
    }

    should("get covid tracker by date should return server domain error if date doesnt exist") {
        val responseError: Response<CovidTrackerDto> = Response.error(400, responseBody)
        coEvery { apiClient.getCovidTrackerByDate(DATE) } throws HttpException(responseError)

        val response = remote.getCovidTrackerByDate(DATE)

        coVerify { apiClient.getCovidTrackerByDate(any()) }
        verify(exactly = 0) { preferences.saveTime() }
        response.shouldBeLeft(DomainError.ServerDomainError)
    }

    should("get covid tracker by date should return no internet error if connection is offline") {
        coEvery { apiClient.getCovidTrackerByDate(DATE) } throws UnknownHostException()

        val response = remote.getCovidTrackerByDate(DATE)

        coVerify { apiClient.getCovidTrackerByDate(any()) }
        verify(exactly = 0) { preferences.saveTime() }
        response.shouldBeLeft(DomainError.NoInternetError)
    }

    should("get covid tracker by date should return generic domain error if required field is null") {
        coEvery { apiClient.getCovidTrackerByDate(any()) } throws JsonDataException()

        val response = remote.getCovidTrackerByDate(DATE)

        coVerify { apiClient.getCovidTrackerByDate(any()) }
        verify(exactly = 0) { preferences.saveTime() }
        assertThat(response.isLeft()).isTrue()
        response.shouldBeLeft(DomainError.GenericDomainError)
    }

    should("get covid tracker by date should return mapper domain error if mapper fails") {
        coEvery { apiClient.getCovidTrackerByDate(DATE) } returns covidTrackerDto.copy(dates = mapOf())

        val response = remote.getCovidTrackerByDate(DATE)

        coVerify { apiClient.getCovidTrackerByDate(any()) }
        verify(exactly = 0) { preferences.saveTime() }
        response.mapLeft { assertThat(it).isInstanceOf(DomainError.MapperDomainError::class.java) }
    }
})