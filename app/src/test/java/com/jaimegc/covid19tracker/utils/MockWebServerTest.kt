package com.jaimegc.covid19tracker.utils

import com.google.common.truth.Truth.assertThat
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import java.net.HttpURLConnection

/**
 *  Adapted from https://github.com/Karumi/KataTODOApiClientKotlin/
 */
open class MockWebServerTest {

    companion object {
        private const val FOLDER_RESPONSES = "responses"
        private const val DEFAULT_PATH = "/api/"
    }

    private var server = MockWebServer()

    @Before
    open fun setUp() {
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    fun enqueueMockResponse(code: Int = HttpURLConnection.HTTP_OK, fileName: String? = null) {
        val mockResponse = MockResponse()
        val fileContent = getContentFromFile(fileName)
        mockResponse.setResponseCode(code)
        mockResponse.setBody(fileContent)
        server.enqueue(mockResponse)
    }

    protected fun assertRequestSentTo(url: String) {
        val request = server.takeRequest()
        assertEquals(url, request.path)
    }

    protected fun assertGetRequestSentTo(url: String) {
        val request = server.takeRequest()
        assertEquals("$DEFAULT_PATH$url", request.path)
        assertEquals("GET", request.method)
    }

    protected fun assertPostRequestSentTo(url: String) {
        val request = server.takeRequest()
        assertEquals(url, request.path)
        assertEquals("POST", request.method)
    }

    protected fun assertPutRequestSentTo(url: String) {
        val request = server.takeRequest()
        assertEquals(url, request.path)
        assertEquals("PUT", request.method)
    }

    protected fun assertDeleteRequestSentTo(url: String) {
        val request = server.takeRequest()
        assertEquals(url, request.path)
        assertEquals("DELETE", request.method)
    }

    protected fun assertRequestSentToContains(vararg paths: String) {
        val request = server.takeRequest()

        for (path in paths) assertThat(request.path).contains(path)
    }

    fun assertRequestContainsHeader(key: String, expectedValue: String, requestIndex: Int = 0) {
        val recordedRequest = getRecordedRequestAtIndex(requestIndex)
        val value = recordedRequest!!.getHeader(key)
        assertEquals(expectedValue, value)
    }

    protected val baseEndpoint: String
        get() = server.url(DEFAULT_PATH).toString()

    protected fun assertRequestBodyEquals(jsonFile: String) {
        val request = server.takeRequest()
        assertEquals(getContentFromFile(jsonFile), request.body.readUtf8())
    }

    private fun getContentFromFile(fileName: String?): String =
        fileName?.let {
            val fileInputStream =
                javaClass.classLoader?.getResourceAsStream("$FOLDER_RESPONSES/$fileName")
            fileInputStream?.bufferedReader()?.readText() ?: ""
        } ?: ""

    private fun getRecordedRequestAtIndex(requestIndex: Int): RecordedRequest? =
        (0..requestIndex).map { server.takeRequest() }.lastOrNull()
}