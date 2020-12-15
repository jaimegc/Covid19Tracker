package com.jaimegc.covid19tracker.apiclient

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.config.ServerApiCovidTrackerConfig
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.utils.MainCoroutineRule
import com.jaimegc.covid19tracker.utils.MockWebServerTest
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import java.net.HttpURLConnection

class CovidTrackerApiClientTest : MockWebServerTest() {

    companion object {
        private const val DATE = "2020-10-02"
        private const val GET_COVID_TRACKER_RESPONSE = "getCovidTrackerResponse.json"
        private const val GET_COVID_TRACKER_NULL_REQUIRED_DATA_ERROR_RESPONSE =
            "getCovidTrackerNullRequiredDataErrorResponse.json"
        private const val GET_COVID_TRACKER_NO_DATA_ERROR_RESPONSE =
            "getCovidTrackerNoDataErrorResponse.json"
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineRule()

    private lateinit var apiClient: CovidTrackerApiClient

    @Before
    override fun setUp() {
        super.setUp()
        apiClient = CovidTrackerApiClient(ServerApiCovidTrackerConfig(baseEndpoint))
    }

    @Test
    fun `get covid trackers by date should return data`() = runBlocking {
        enqueueMockResponse(HttpURLConnection.HTTP_OK, GET_COVID_TRACKER_RESPONSE)

        val dto = apiClient.getCovidTrackerByDate(DATE)
        assertThat(dto).isNotNull()
        assertThat(dto).isInstanceOf(CovidTrackerDto::class.java)
    }

    @Test
    fun `get covid trackers by date should call the correct endpoint`() = runBlocking {
        enqueueMockResponse(HttpURLConnection.HTTP_OK, GET_COVID_TRACKER_RESPONSE)

        apiClient.getCovidTrackerByDate(DATE)

        assertGetRequestSentTo(DATE)
    }

    @Test(expected = JsonDataException::class)
    fun `get covid trackers by date with null required data should return json data exception`(): Unit = runBlocking {
        enqueueMockResponse(
            HttpURLConnection.HTTP_OK, GET_COVID_TRACKER_NULL_REQUIRED_DATA_ERROR_RESPONSE
        )

        apiClient.getCovidTrackerByDate(DATE)
    }

    @Test(expected = HttpException::class)
    fun `get covid trackers by date with empty data should return http exception`(): Unit = runBlocking {
        enqueueMockResponse(
            HttpURLConnection.HTTP_NOT_FOUND, GET_COVID_TRACKER_NO_DATA_ERROR_RESPONSE
        )

        apiClient.getCovidTrackerByDate(DATE)
    }

    @Test(expected = HttpException::class)
    fun `get covid trackers by date with server error should return http exception`(): Unit = runBlocking {
        enqueueMockResponse(HttpURLConnection.HTTP_INTERNAL_ERROR)

        apiClient.getCovidTrackerByDate(DATE)
    }

    @Test
    fun `get covid trackers by date as response should return data`() = runBlocking {
        enqueueMockResponse(HttpURLConnection.HTTP_OK, GET_COVID_TRACKER_RESPONSE)

        val response = apiClient.getCovidTrackerByDateAsResponse(DATE)

        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_OK)
        assertThat(response.body()).isInstanceOf(CovidTrackerDto::class.java)
    }

    @Test
    fun `get covid trackers by date as response should call the correct endpoint`() = runBlocking {
        enqueueMockResponse(HttpURLConnection.HTTP_OK, GET_COVID_TRACKER_RESPONSE)

        apiClient.getCovidTrackerByDateAsResponse(DATE)

        assertGetRequestSentTo(DATE)
    }

    @Test(expected = JsonDataException::class)
    fun `get covid trackers by date as response with null required data should return json data exception`(): Unit = runBlocking {
        enqueueMockResponse(
            HttpURLConnection.HTTP_OK, GET_COVID_TRACKER_NULL_REQUIRED_DATA_ERROR_RESPONSE
        )

        apiClient.getCovidTrackerByDateAsResponse(DATE)
    }

    @Test
    fun `get covid trackers by date as responsse with empty data should have code 404 and null body`(): Unit = runBlocking {
        enqueueMockResponse(
            HttpURLConnection.HTTP_NOT_FOUND, GET_COVID_TRACKER_NO_DATA_ERROR_RESPONSE
        )

        val response = apiClient.getCovidTrackerByDateAsResponse(DATE)

        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_NOT_FOUND)
        assertThat(response.body()).isNull()
    }

    @Test
    fun `get covid trackers by date as response with server error should have code 500 and null body`(): Unit = runBlocking {
        enqueueMockResponse(HttpURLConnection.HTTP_INTERNAL_ERROR)

        val response = apiClient.getCovidTrackerByDateAsResponse(DATE)

        assertThat(response.code()).isEqualTo(HttpURLConnection.HTTP_INTERNAL_ERROR)
        assertThat(response.body()).isNull()
    }
}