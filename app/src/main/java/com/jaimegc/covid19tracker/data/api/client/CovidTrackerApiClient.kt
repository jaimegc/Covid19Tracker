package com.jaimegc.covid19tracker.data.api.client

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

    suspend fun getCovidTrackerByDateAsResponse(date: String): Response<CovidTrackerDto> =
        getApi(CovidTrackerRest::class.java).getCovidTrackerByDateAsResponse(date)
}