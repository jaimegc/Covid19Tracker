package com.jaimegc.covid19tracker.data.api.client

import com.google.gson.JsonObject
import com.jaimegc.covid19tracker.data.api.config.ServerApiClient
import com.jaimegc.covid19tracker.data.api.config.ServerApiCovidTrackerConfig
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.data.api.rest.CovidTrackerRest
import retrofit2.Response


class CovidTrackerApiClient(
    serverApiConfig: ServerApiCovidTrackerConfig
) : ServerApiClient(serverApiConfig) {

    suspend fun getCovidTrackerByDate(date: String): CovidTrackerDto =
        getApi(CovidTrackerRest::class.java).getCovidTrackerByDate(date)

    suspend fun getCovidTrackerByDateAsJson(date: String): Response<JsonObject> =
        getApi(CovidTrackerRest::class.java).getCovidTrackerByDateAsJson(date)
}