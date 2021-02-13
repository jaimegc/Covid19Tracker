package com.jaimegc.covid19tracker.apiclient.kotest

import com.google.common.truth.Truth.assertThat
import com.jaimegc.covid19tracker.ModelFactoryTest.DATE
import com.jaimegc.covid19tracker.data.api.client.CovidTrackerApiClient
import com.jaimegc.covid19tracker.data.api.config.ServerApiCovidTrackerConfig
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.squareup.moshi.JsonDataException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.HttpException
import java.net.HttpURLConnection

class CovidTrackerApiClientKotestTest : ShouldSpec({

    lateinit var baseEndpoint: String

    val GET_COVID_TRACKER_RESPONSE = "getCovidTrackerResponse.json"
    val GET_COVID_TRACKER_NULL_REQUIRED_DATA_ERROR_RESPONSE =
        "getCovidTrackerNullRequiredDataErrorResponse.json"
    val GET_COVID_TRACKER_NO_DATA_ERROR_RESPONSE =
        "getCovidTrackerNoDataErrorResponse.json"

    lateinit var apiClient: CovidTrackerApiClient

    val server = MockWebServer()

    beforeTest {
        server.start()
        baseEndpoint = server.url(DEFAULT_PATH).toString()
        apiClient = CovidTrackerApiClient(ServerApiCovidTrackerConfig(baseEndpoint))
    }

    afterTest {
        server.shutdown()
    }

    should("get covid trackers by date should return data") {
        val fileInputStream =
            javaClass.classLoader?.getResourceAsStream("${FOLDER_RESPONSES}/$GET_COVID_TRACKER_RESPONSE")
        enqueueMockResponse(
            server, HttpURLConnection.HTTP_OK, fileInputStream?.bufferedReader()?.readText() ?: ""
        )

        val dto = apiClient.getCovidTrackerByDate(DATE)
        dto shouldNotBe null
        assertThat(dto).isInstanceOf(CovidTrackerDto::class.java)
    }

    should("get covid trackers by date should call the correct endpoint") {
        val fileInputStream =
            javaClass.classLoader?.getResourceAsStream("${FOLDER_RESPONSES}/$GET_COVID_TRACKER_RESPONSE")
        enqueueMockResponse(
            server, HttpURLConnection.HTTP_OK, fileInputStream?.bufferedReader()?.readText() ?: ""
        )

        apiClient.getCovidTrackerByDate(DATE)

        assertGetRequestSentTo(server, DATE)
    }

    should("get covid trackers by date with null required data should throw json data exception") {
        val fileInputStream =
            javaClass.classLoader?.getResourceAsStream("${FOLDER_RESPONSES}/$GET_COVID_TRACKER_NULL_REQUIRED_DATA_ERROR_RESPONSE")
        enqueueMockResponse(
            server, HttpURLConnection.HTTP_OK, fileInputStream?.bufferedReader()?.readText() ?: ""
        )

        shouldThrow<JsonDataException> {
            apiClient.getCovidTrackerByDate(DATE)
        }
    }

    should("get covid trackers by date with empty data should throw http exception") {
        val fileInputStream =
            javaClass.classLoader?.getResourceAsStream("${FOLDER_RESPONSES}/$GET_COVID_TRACKER_NO_DATA_ERROR_RESPONSE")
        enqueueMockResponse(
            server, HttpURLConnection.HTTP_NOT_FOUND, fileInputStream?.bufferedReader()?.readText() ?: ""
        )

        shouldThrow<HttpException> {
            apiClient.getCovidTrackerByDate(DATE)
        }
    }

    should("get covid trackers by date with server error should throw http exception") {
        enqueueMockResponse(server, HttpURLConnection.HTTP_INTERNAL_ERROR, "")

        shouldThrow<HttpException> {
            apiClient.getCovidTrackerByDate(DATE)
        }
    }

    should("get covid trackers by date as response should return data") {
        val fileInputStream =
            javaClass.classLoader?.getResourceAsStream("${FOLDER_RESPONSES}/$GET_COVID_TRACKER_RESPONSE")
        enqueueMockResponse(
            server, HttpURLConnection.HTTP_OK, fileInputStream?.bufferedReader()?.readText() ?: ""
        )

        val response = apiClient.getCovidTrackerByDateAsResponse(DATE)

        response.code() shouldBe HttpURLConnection.HTTP_OK
        assertThat(response.body()).isInstanceOf(CovidTrackerDto::class.java)
    }

    should("get covid trackers by date as response should call the correct endpoint") {
        val fileInputStream =
            javaClass.classLoader?.getResourceAsStream("${FOLDER_RESPONSES}/$GET_COVID_TRACKER_RESPONSE")
        enqueueMockResponse(
            server, HttpURLConnection.HTTP_OK, fileInputStream?.bufferedReader()?.readText() ?: ""
        )

        apiClient.getCovidTrackerByDateAsResponse(DATE)

        assertGetRequestSentTo(server, DATE)
    }

    should("get covid trackers by date as response with null required data should throw json data exception") {
        val fileInputStream =
            javaClass.classLoader?.getResourceAsStream("${FOLDER_RESPONSES}/$GET_COVID_TRACKER_NULL_REQUIRED_DATA_ERROR_RESPONSE")
        enqueueMockResponse(
            server, HttpURLConnection.HTTP_OK, fileInputStream?.bufferedReader()?.readText() ?: ""
        )

        shouldThrow<JsonDataException> {
            apiClient.getCovidTrackerByDateAsResponse(DATE)
        }
    }

    should("get covid trackers by date as response with empty data should have code 404 and null body") {
        val fileInputStream =
            javaClass.classLoader?.getResourceAsStream("${FOLDER_RESPONSES}/$GET_COVID_TRACKER_NO_DATA_ERROR_RESPONSE")
        enqueueMockResponse(
            server, HttpURLConnection.HTTP_NOT_FOUND, fileInputStream?.bufferedReader()?.readText() ?: ""
        )

        val response = apiClient.getCovidTrackerByDateAsResponse(DATE)

        response.code() shouldBe HttpURLConnection.HTTP_NOT_FOUND
        response.body() shouldBe null
    }

    should("get covid trackers by date as response with server error should have code 500 and null body") {
        val fileInputStream =
            javaClass.classLoader?.getResourceAsStream("${FOLDER_RESPONSES}/$GET_COVID_TRACKER_NO_DATA_ERROR_RESPONSE")
        enqueueMockResponse(
            server, HttpURLConnection.HTTP_INTERNAL_ERROR, fileInputStream?.bufferedReader()?.readText() ?: ""
        )

        val response = apiClient.getCovidTrackerByDateAsResponse(DATE)

        response.code() shouldBe HttpURLConnection.HTTP_INTERNAL_ERROR
        response.body() shouldBe null
    }
})

private const val FOLDER_RESPONSES = "responses"
private const val DEFAULT_PATH = "/api/"

private fun enqueueMockResponse(server: MockWebServer, code: Int = HttpURLConnection.HTTP_OK, fileContent: String) {
    val mockResponse = MockResponse()
    mockResponse.setResponseCode(code)
    mockResponse.setBody(fileContent)
    server.enqueue(mockResponse)
}

private fun assertGetRequestSentTo(server: MockWebServer, url: String) {
    val request = server.takeRequest()
    request.path shouldBe "${DEFAULT_PATH}$url"
    request.method shouldBe "GET"
}