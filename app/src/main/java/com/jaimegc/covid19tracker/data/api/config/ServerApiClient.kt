package com.jaimegc.covid19tracker.data.api.config

import com.jaimegc.covid19tracker.data.api.error.*
import retrofit2.Call
import retrofit2.Response


open class ServerApiClient(private val serverApiConfig: ServerApiConfig) {

    companion object {
        const val RESPONSE_ERROR_500: Int = 500
        const val RESPONSE_ERROR_404: Int = 404
        const val RESPONSE_ERROR_403: Int = 403
    }

    fun <T> getApi(apiRest: Class<T>): T =
        serverApiConfig.retrofit.create(apiRest)

   suspend fun <T : Any> suspendApiCall(call: suspend () -> Response<T>?): Response<T>? =
        try {
            call.invoke()
        } catch (e: Exception) {
            null
        }

    fun <T> execute(call: Call<T>): T? {
        var response: Response<T>

        try {
            response = call.execute()
        } catch (e: Exception) {
            throw ServerConnectionApiException
        }

        return if (response.isSuccessful) {
            if (response.body() != null) {
                response.body()
            } else {
                null
            }
        } else {
            response.parseErrorCode()
            null
        }
    }

    fun <T> handleResponse(response: Response<T>?): T? {
        response?.let {
            return if (it.isSuccessful) {
                if (it.body() != null) {
                    it.body()
                } else {
                    throw ServerConnectionApiException
                }
            } else {
                response.parseErrorCode()
                null
            }
        } ?: throw ServerConnectionApiException
    }

    private fun <T> Response<T>.parseErrorCode() {
        when (this.code()) {
            RESPONSE_ERROR_500 -> throw Server500ApiException
            RESPONSE_ERROR_404 -> throw Server404ApiException
            RESPONSE_ERROR_403 -> throw Server403ApiException
            else -> throw ServerUnknownApiException
        }
    }
}