package com.jaimegc.covid19tracker.data.api.client

import com.jaimegc.covid19tracker.data.api.config.ServerApiClient
import com.jaimegc.covid19tracker.data.api.config.ServerApiCovidTrackerConfig
import com.jaimegc.covid19tracker.data.api.model.CovidTrackerDto
import com.jaimegc.covid19tracker.data.api.rest.CovidTrackerRest


class CovidTrackerApiClient(
    serverApiConfig: ServerApiCovidTrackerConfig
) : ServerApiClient(serverApiConfig) {

    suspend fun getCovidTrackerLast(): CovidTrackerDto =
        getApi(CovidTrackerRest::class.java).getCovidTrackerByDate("2020-04-10")
}