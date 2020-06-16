package com.jaimegc.covid19tracker.data.api.config

open class ServerApiClient(private val serverApiConfig: ServerApiConfig) {

    fun <T> getApi(apiRest: Class<T>): T =
        serverApiConfig.retrofit.create(apiRest)
}