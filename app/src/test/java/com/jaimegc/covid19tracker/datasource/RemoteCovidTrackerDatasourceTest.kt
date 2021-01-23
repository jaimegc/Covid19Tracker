package com.jaimegc.covid19tracker.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.DATE
import com.jaimegc.covid19tracker.ModelFactoryTest.covidTrackerDto
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.data.datasource.RemoteCovidTrackerDatasource
import com.jaimegc.covid19tracker.data.preference.CovidTrackerPreferences
import com.jaimegc.covid19tracker.domain.model.DomainError
import com.jaimegc.covid19tracker.domain.model.toDomain
import com.jaimegc.covid19tracker.util.MainCoroutineRule
import com.squareup.moshi.JsonDataException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

class RemoteCovidTrackerDatasourceTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineRule()

    @MockK(relaxed = true)
    private lateinit var apiClient: CovidTrackerApiClient
    @MockK(relaxed = true)
    private lateinit var preferences: CovidTrackerPreferences

    @MockK(relaxed = true)
    lateinit var responseBody: ResponseBody

    private lateinit var remote: RemoteCovidTrackerDatasource

    @Before
    fun init() {
        MockKAnnotations.init(this)
        remote = RemoteCovidTrackerDatasource(apiClient, preferences)
    }

    @Test
    fun `get covid tracker by date should return covid tracker if date exists`() = runBlockingTest {
        coEvery { apiClient.getCovidTrackerByDate(any()) } returns covidTrackerDto

        val data = remote.getCovidTrackerByDate(DATE)

        coVerify { apiClient.getCovidTrackerByDate(any()) }
        verify { preferences.saveTime() }
        assertThat(data.isRight()).isTrue()
        data.map { assertThat(it).isEqualTo(covidTrackerDto.toDomain(DATE)) }
    }

    @Test
    fun `get covid tracker by date should return server domain error if date doesnt exist`() = runBlockingTest {
        val responseError: Response<CovidTrackerDto> = Response.error(400, responseBody)
        coEvery { apiClient.getCovidTrackerByDate(DATE) } throws HttpException(responseError)

        val response = remote.getCovidTrackerByDate(DATE)

        coVerify { apiClient.getCovidTrackerByDate(any()) }
        verify(exactly = 0) { preferences.saveTime() }
        assertThat(response.isLeft()).isTrue()
        response.mapLeft { assertThat(it).isEqualTo(DomainError.ServerDomainError) }
    }

    @Test
    fun `get covid tracker by date should return no internet error if connection is offline`() = runBlockingTest {
        coEvery { apiClient.getCovidTrackerByDate(DATE) } throws UnknownHostException()

        val response = remote.getCovidTrackerByDate(DATE)

        coVerify { apiClient.getCovidTrackerByDate(any()) }
        verify(exactly = 0) { preferences.saveTime() }
        assertThat(response.isLeft()).isTrue()
        response.mapLeft { assertThat(it).isEqualTo(DomainError.NoInternetError) }
    }

    @Test
    fun `get covid tracker by date should return generic domain error if required field is null`() = runBlockingTest {
        coEvery { apiClient.getCovidTrackerByDate(any()) } throws JsonDataException()

        val response = remote.getCovidTrackerByDate(DATE)

        coVerify { apiClient.getCovidTrackerByDate(any()) }
        verify(exactly = 0) { preferences.saveTime() }
        assertThat(response.isLeft()).isTrue()
        response.mapLeft { assertThat(it).isEqualTo(DomainError.GenericDomainError) }
    }

    @Test
    fun `get covid tracker by date should return mapper domain error if mapper fails`() = runBlockingTest {
        coEvery { apiClient.getCovidTrackerByDate(DATE) } returns covidTrackerDto.copy(dates = mapOf())

        val response = remote.getCovidTrackerByDate(DATE)

        coVerify { apiClient.getCovidTrackerByDate(any()) }
        verify(exactly = 0) { preferences.saveTime() }
        assertThat(response.isLeft()).isTrue()
        response.mapLeft { assertThat(it).isInstanceOf(DomainError.MapperDomainError::class.java) }
    }
}